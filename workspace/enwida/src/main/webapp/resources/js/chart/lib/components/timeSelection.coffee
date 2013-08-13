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

      @fillTimeRange()
      @fillDatePickers dateLimits

      # Apply defaults
      defaultRange = data.defaults.timeRange
      rangeLiteral = TimeUtils.timeRangeLiteral defaultRange 
      @select("timeRange").val(rangeLiteral).change()
      @setFromDate new Date defaultRange.from

    @setupEvents = ->
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

      # Create bus from all date pickers
      fromDateStreams = @$node.find(".datepicker-generic .from").map (_, e) ->
        $(e).asEventStream("changeDate").map (e) -> e.date
      fromDateStream = Bacon.mergeAll(fromDateStreams...)
      fromDateBus = new Bacon.Bus()
      fromDateBus.plug fromDateStream

      # Keep datepicker dates in sync
      syncStream = timeRangeStream.toProperty().sampledBy fromDateBus, (timeRange, date) -> [date, timeRange]
      syncStream.onValue (args) => @syncDatepickers args...

      # Create date range emitting stream
      dateRangeStream = Bacon.combineWith ((fromDate, timeRange) =>
        toDate = TimeUtils.addRange fromDate, timeRange
        from: fromDate, to: toDate
      ), fromDateBus, timeRangeStream
      dateRangeStream.onValue (timeRange) =>
        @trigger "timeSelectionChanged", timeRange: timeRange

      # Next/prev buttons
      @$node.find(".nextPeriode").click => @nextPeriode()
      @$node.find(".prevPeriode").click => @prevPeriode()

      # Time restrictions
      timeRestrictionStream = @$node.asEventStream "timeRestrictions", (_, v) -> v
      timeRestrictionStream.onValue (timeRange) =>
        elements = @$node.find(".datepicker-generic .from")
        elements.datepicker "setStartDate", new Date timeRange.from
        elements.datepicker "setEndDate", new Date timeRange.to

      @attr.fromDateBus = fromDateBus

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

      # Trigger first change element
      element.change()

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

    @syncDatepickers = (date, sender) ->
      for timeRange in @timeRanges
        continue if timeRange is sender

        element = @getDatepickerElement timeRange
        newDate = switch sender
          when "Day"
            if timeRange is "Week"
              TimeUtils.getWeekStart date
            else
              date
          when "Week"
            result = new Date element.data("datepicker").date
            result.setMonth date.getMonth()
            result.setFullYear date.getFullYear()
            result
          when "Month"
            result = new Date element.data("datepicker").date
            result.setMonth date.getMonth()
            result.setFullYear date.getFullYear()
            result
          when "Year"
            result = new Date element.data("datepicker").date
            result.setFullYear date.getFullYear()
            result
        element.datepicker "setDate", newDate

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
      @syncDatepickers date, @getCurrentTimeRange()

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

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data


