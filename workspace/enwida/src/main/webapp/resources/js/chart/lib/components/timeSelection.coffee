define ["../util/time_utils", "../util/resolution"], (TimeUtils, Resolution) ->

  flight.component ->

    @dateFormat       = d3.time.format "%Y-%m-%d"
    @datePickerFormat = "yyyy-mm-dd"
    @timeRanges       = ["Day", "Week", "Month", "Year"]
    @viewModes        =
      Day: "days"
      Week: "days"
      Month: "months"
      Year: "years"
    @datePickerFormats =
      Day: "yyyy-mm-dd"
      Week: "yyyy-mm-dd"
      Month: "yyyy-mm"
      Year: "yyyy"

    @refresh = (data) ->
      @attr.navigationData = data
      @createElements()
      @setupEvents()

      dateLimits =
        from : new Date data.timeRangeMax.from
        to   : new Date data.timeRangeMax.to

      @fillDatePickers dateLimits
      @fillTimeRange()

      # Apply defaults
      defaultRange = data.defaults.timeRange
      rangeLiteral = TimeUtils.timeRangeLiteral defaultRange 

      # Push default from date to all datepicker busses
      for s in _(@attr.streams).values()
        s.in.push new Date defaultRange.from

      # Select default time range trigger initial loading
      @select("timeRange").val(rangeLiteral).change()

    @setupEvents = ->
      # Setup time range stream
      timeRangeElement = @select("timeRange")
      timeRangeStream = timeRangeElement.asEventStream "change", -> timeRangeElement.val()

      # Setup time (range) restrictions stream
      timeRestrictionStream = @$node.asEventStream "timeRestrictions", (_, v) -> v

      # Setup next/prev button streams
      prevStream = @select("prevPeriode").asEventStream "click"
      nextStream = @select("nextPeriode").asEventStream "click"

      # Setup datepicker boundaries stream
      datepickerBoundariesStream = timeRangeStream.combine timeRestrictionStream,
        (timeRange, timeRestriction) =>
          from = new Date timeRestriction.from
          to   = new Date timeRestriction.to
          # TODO: inclusive/exclusive problem
          #to   = TimeUtils.subtractRange timeRestriction.to, timeRange
          [from, to]

      # Setup datepicker busses / streams
      @attr.streams = dpStreams = {}
      @timeRanges.forEach (timeRange) =>
        streams = dpStreams[timeRange] = {}
        streams.in    = new Bacon.Bus()
        streams.sync  = new Bacon.Bus()
        streams.out   = new Bacon.Bus()

        Bacon.combineAsArray(streams.sync, streams.in).sampledBy(timeRangeStream, -> arguments)
          .onValue ([[[syncDate, sender], date], tr]) =>
            if tr is timeRange
              date = @mergeDate date, syncDate, tr, sender
              streams.in.push date
          
        streams.valid = datepickerBoundariesStream.combine streams.in,
          ([from, to], date) ->
            timeRestriction = from: from, to: to
            date = TimeUtils.nearestInTimeRange date, timeRestriction
            date = TimeUtils.normalizedToTimeRange date, timeRange
            date = TimeUtils.nearestInTimeRange date, timeRestriction
            [date, timeRange]

        streams.currentValid = Bacon.combineAsArray(streams.valid, timeRangeStream).flatMap \
          ([[date, timeRange], currentTimeRange]) =>
            if timeRange is currentTimeRange
              Bacon.once [date, timeRange]
            else
              Bacon.never()
        streams.fire = streams.valid.sampledBy streams.out, ([date,_]) -> date

      # Setup from date stream
      fireStreams = _(dpStreams).chain().values().map((s) -> s.fire).value()
      fromDateStream = Bacon.mergeAll fireStreams...

      # Setup valid incoming date stream
      validDateStreams = _(dpStreams).chain().values().map((s) -> s.valid.toEventStream()).value()
      validDateStream = Bacon.mergeAll validDateStreams...

      # Setup
      currentValidDateStreams = _(dpStreams).chain().values().map((s) -> s.currentValid.toEventStream()).value()
      currentValidDateStream = Bacon.mergeAll currentValidDateStreams

      # Prev button
      currentValidDateStream.toProperty().sampledBy(prevStream)
        .onValue ([date, timeRange]) =>
          date = TimeUtils.subtractRange date, timeRange
          dpStreams[timeRange].in.push date
          dpStreams[timeRange].out.push()

      # Next button
      currentValidDateStream.toProperty().sampledBy(nextStream)
        .onValue ([date, timeRange]) =>
          date = TimeUtils.addRange date, timeRange
          dpStreams[timeRange].in.push date
          dpStreams[timeRange].out.push()

      # Handle new boundaries event
      datepickerBoundariesStream.onValue ([from, to]) =>
        for timeRange in @timeRanges
          datepicker = @getDatepickerElement timeRange
          datepicker.datepicker "setStartDate", from
          datepicker.datepicker "setEndDate", to

      # Handle new valid date event
      validDateStream.onValue ([date, timeRange]) =>
        datepicker = @getDatepickerElement timeRange
        datepicker.datepicker "setDate", date

      # Sync to other date pickers
      timeRangeStream.toProperty().sampledBy(fromDateStream, -> arguments)
        .onValue ([timeRange, date]) =>
          for tr in @timeRanges
            dpStreams[tr].sync.push [date, timeRange]

      # Setup date range stream
      dateRangeStream = Bacon.combineWith (([fromDate, timeRange], timeRestriction) =>
        toDate = TimeUtils.addRange fromDate, timeRange
        toDate = TimeUtils.nearestInTimeRange toDate, timeRestriction
        [fromDate, toDate]
      ), currentValidDateStream, timeRestrictionStream

      # Setup fire bus
      fireStream = @$node.asEventStream "requestTimeSelection"
      fireBus = @attr.fireBus = new Bacon.Bus()
      fireBus.plug fireStream
      fireBus.plug fromDateStream
      fireBus.plug timeRangeStream

      # Handle event firing
      dateRangeStream.sampledBy(fireBus)
        .onValue ([from, to]) =>
          @trigger "timeSelectionChanged",
            timeRange:
              from: from
              to  : to

      # Bind the datepicker actions
      @timeRanges.forEach (timeRange) =>
        datepicker = @getDatepickerElement timeRange
        datepicker.on "changeDate", (e) =>
          dpStreams[timeRange].in.push e.date
          dpStreams[timeRange].out.push()

      # Show datapicker when clicking on icon
      @$node.find(".datepicker-generic").each (_, datePicker) ->
        $(datePicker).find(".fromIcon").click ->
          $(datePicker).find(".from").datepicker "show"

      # Bind the time value of the time range selector to the datapicker's
      # visibility
      timeRangeStream = @select("timeRange").asEventStream("change").map (e) -> $(e.target).val()
      timeRangeStream.onValue (currentTimeRange) =>
        for timeRange in @timeRanges
          datePicker = @$node.find ".datepicker-#{timeRange}"
          if timeRange is currentTimeRange
            datePicker.show()
          else
            datePicker.hide()

    @createElements = ->
      @$node.empty()

      @timeRanges.forEach (timeRange) =>
        @$node.append($("<div>").addClass("input-append")
          .addClass("datepicker-generic").addClass("datepicker-#{timeRange}")
          .append($("<input>").attr("type", "text").addClass("from").attr("readonly", true))
          .append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon")
            .append($("<i>").addClass("icon-th"))).hide())

      @$node.append($("<div>").addClass("prevPeriode")
        .append($("<i>").addClass("icon-chevron-left")))

      @$node.append $("<select>").addClass("timerange")

      @$node.append($("<div>").addClass("nextPeriode")
        .append($("<i>").addClass("icon-chevron-right")))

    @fillTimeRange = ->
      element = @select("timeRange")
      for timeRange in @timeRanges
        continue unless _(@attr.navigationData.timeRanges).contains(timeRange)
        timeRangeName = @attr.navigationData.localizations.timeRanges[timeRange]
        element.append($("<option>").val(timeRange).text(timeRangeName))

    @fillDatePickers = (limits={}) ->
      @timeRanges.forEach (timeRange) =>
        element = @getDatepickerElement timeRange
        element.datepicker
          format        : @datePickerFormats[timeRange]
          weekStart     : 1
          calendarWeeks : timeRange is "Week"
          weekselect    : timeRange is "Week"
          viewMode      : @viewModes[timeRange]
          minViewMode   : @viewModes[timeRange]
          startDate     : limits.from ? "1900-01-01"
          endDate       : limits.to ? new Date()

    @mergeDate = (date, syncDate, timeRange, sender) ->
      switch sender
        when "Day"
          if timeRange is "Week"
            TimeUtils.getWeekStart syncDate
          else
            syncDate
        when "Week"
          result = new Date date
          result.setMonth syncDate.getMonth()
          result.setFullYear syncDate.getFullYear()
          result
        when "Month"
          result = new Date date
          result.setMonth syncDate.getMonth()
          result.setFullYear syncDate.getFullYear()
          result
        when "Year"
          result = new Date date
          result.setFullYear syncDate.getFullYear()
          result

    @getDatepickerElement = (timeRange) ->
      @$node.find ".datepicker-#{timeRange} .from"

    @getCurrentDatePicker = ->
      timeRange = @getCurrentTimeRange()
      @getDatepickerElement timeRange

    @getFromDate = ->
      datePicker = @getCurrentDatePicker()
      datePicker.data("datepicker").date

    @setFromDate = (date) ->
      datePicker = @getCurrentDatePicker()
      datePicker.datepicker "setDate", date
      @attr.fromDateBus.push date

    @prevPeriode = ->
      modifier = TimeUtils.getDateModifier @getCurrentTimeRange()
      modifier.backwards = true
      date = TimeUtils.modifyDate @getFromDate(), modifier
      @setFromDate date

    @nextPeriode = ->
      time = TimeUtils.addRange @getFromDate(), @getCurrentTimeRange()
      @setFromDate time

    @getCurrentTimeRange = ->
      @select("timeRange").val()

    @defaultAttrs
      timeRange: ".timerange"
      prevPeriode: ".prevPeriode"
      nextPeriode: ".nextPeriode"

    @after "initialize", ->
      @attr.fromDateBus = new Bacon.Bus()
      @on "refresh", (_, opts) => @refresh opts.data


