define ["../util/time_utils"], (TimeUtils) ->

  flight.component ->

    @dateFormat       = d3.time.format "%Y-%m-%d"
    @datePickerFormat = "yyyy-mm-dd"

    @refresh = (data) ->
      @attr.navigationData = data
      @createElements()
      @setupEvents()
      @fillDatePicker()

      # Apply defaults
      defaultRange = data.defaults.timeRange
      @attr.fromDateBus.push new Date defaultRange.from
      @attr.toDateBus.push new Date defaultRange.to

    @setupEvents = ->
      # Create bus for date pickers
      fromDateStream = @select("fromPicker").asEventStream "changeDate"
      @attr.fromDateBus = fromDateBus = new Bacon.Bus()
      fromDateBus.plug fromDateStream.map (e) -> e.date

      toDateStream = @select("toPicker").asEventStream "changeDate"
      @attr.toDateBus = toDateBus = new Bacon.Bus()
      toDateBus.plug toDateStream.map (e) -> e.date

      # Create resolution stream
      resolutionStream = @select("resolution").asEventStream("change")
        .map => @select("resolution").val()

      # Create time restriction stream
      timeRestrictionStream = @$node.asEventStream "timeRestrictions", (_, v) -> v

      # Create fire request stream
      fireRequestStream = @$node.asEventStream "requestTimeSelection", (_, v) -> v

      # Create valid date streams
      validFromDateStream = Bacon.combineWith ((timeRestriction, resolution, date) =>
        TimeUtils.nearestInTimeRange date, timeRestriction.timeRange
      ), timeRestrictionStream, resolutionStream, fromDateBus

      validToDateStream = Bacon.combineWith ((timeRestriction, resolution, date) =>
        TimeUtils.nearestInTimeRange date, timeRestriction.timeRange
      ), timeRestrictionStream, resolutionStream, toDateBus

      # Create date range emitting stream
      dateRangeStream = Bacon.combineWith ((fromDate, toDate, resolution) =>
        timeRange:
          from: fromDate
          to: toDate
        resolution: resolution
      ), validFromDateStream, validToDateStream, resolutionStream

      # Create fire bus
      @attr.fireBus = fireBus = new Bacon.Bus()
      fireBus.plug fireRequestStream
      fireBus.plug resolutionStream

      # Handle new time restriction
      timeRestrictionStream.onValue (restriction) =>
        element = @select "resolution"
        oldValue = element.val()
        element.empty()
        for resolution in restriction.resolution
          element.append($("<option>")
            .attr("value", resolution)
            .text(@attr.navigationData.localizations.resolutions[resolution]))
        element.val oldValue
        element.change()

        elements = @select "datepickers"
        elements.datepicker "setStartDate", new Date restriction.timeRange.from
        elements.datepicker "setEndDate", new Date restriction.timeRange.to

      # Handle valid dates
      validFromDateStream.onValue (date) =>
        @select("fromPicker").datepicker "setDate", new Date date

      validToDateStream.onValue (date) =>
        @select("toPicker").datepicker "setDate", new Date date

      # Handle passing events to parent
      dateRangeStream.sampledBy(fireBus)
        .onValue (selections) =>
          @trigger "timeSelectionChanged", selections

      fromDateStream.onValue -> fireBus.push()
      toDateStream.onValue   -> fireBus.push()

      # Show datapicker when clicking on icon
      @$node.find(".datepicker-generic").each (_, datePicker) ->
        $(datePicker).find(".icon").click ->
          $(datePicker).find(".picker").datepicker "show"

    @createElements = ->
      @$node.empty()

      @$node.append($("<div>").addClass("input-append")
        .addClass("datepicker-generic").addClass("datepicker-from")
        .append($("<input>").attr("type", "text").addClass("picker").attr("readonly", true))
        .append($("<span>").addClass("add-on").addClass("calendar").addClass("icon")
          .append($("<i>").addClass("icon-th"))))

      @$node.append($("<div>").addClass("input-append")
        .addClass("datepicker-generic").addClass("datepicker-to")
        .append($("<input>").attr("type", "text").addClass("picker").attr("readonly", true))
        .append($("<span>").addClass("add-on").addClass("calendar").addClass("icon")
          .append($("<i>").addClass("icon-th"))))

      @$node.append($("<select>").addClass("resolution"))

    @fillDatePicker = (limits={}) ->
      element = @select("datepickers")
      element.datepicker
        format        : @datePickerFormat
        weekStart     : 1
        viewMode      : "days"
        startDate     : limits.from ? "1900-01-01"
        endDate       : limits.to ? new Date()
        language      : @attr.navigationData.localizations.locale ? "de"

    @defaultAttrs
      timeRange: ".timerange"
      datepickers: ".datepicker-generic .picker"
      fromPicker: ".datepicker-from .picker"
      toPicker: ".datepicker-to .picker"
      resolution: ".resolution"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data

