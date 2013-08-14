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
      @setFromDate new Date defaultRange.from
      @setToDate new Date defaultRange.to

    @setupEvents = ->
      # Show datapicker when clicking on icon
      @$node.find(".datepicker-generic").each (_, datePicker) ->
        $(datePicker).find(".icon").click ->
          $(datePicker).find(".picker").datepicker "show"

      # Create bus for date pickers
      fromDateStream = @select("fromPicker").asEventStream "changeDate"
      fromDateBus = new Bacon.Bus()
      fromDateBus.plug fromDateStream.map (e) -> e.date

      toDateStream = @select("toPicker").asEventStream "changeDate"
      toDateBus = new Bacon.Bus()
      toDateBus.plug toDateStream.map (e) -> e.date

      # Create bus for resolutions
      resolutionStream = @select("resolution").asEventStream "change"
      resolutionBus = new Bacon.Bus()
      resolutionBus.plug resolutionStream.map (e) -> $(e.target).val()

      # Create date range emitting stream
      dateRangeStream = Bacon.combineWith ((fromDate, toDate, resolution) =>
        timeRange:
          from: fromDate
          to: toDate
        resolution: resolution
      ), fromDateBus, toDateBus, resolutionBus
      dateRangeStream.onValue (selections) =>
        @trigger "timeSelectionChanged", selections

      # Time restrictions
      timeRestrictionStream = @$node.asEventStream "timeRestrictions", (_, v) -> v
      timeRestrictionStream.onValue (restrictions) =>
        element = @select "resolution"
        oldValue = element.val()
        element.empty()
        for resolution in restrictions.resolution
          element.append($("<option>")
            .attr("value", resolution)
            .text(@attr.navigationData.localizations.resolutions[resolution]))
        element.val oldValue
        element.change()

        elements = @select "datepickers"
        elements.datepicker "setStartDate", new Date restrictions.timeRange.from
        elements.datepicker "setEndDate", new Date restrictions.timeRange.to

      @attr.fromDateBus = fromDateBus
      @attr.toDateBus = toDateBus

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

    @getCurrentDate = ->
      datePicker = @select("datepicker")
      datePicker.data("datepicker").date

    @setFromDate = (date) ->
      @select("fromPicker").datepicker "setDate", date
      @attr.fromDateBus.push date

    @setToDate = (date) ->
      @select("toPicker").datepicker "setDate", date
      @attr.toDateBus.push date

    @defaultAttrs
      timeRange: ".timerange"
      datepickers: ".datepicker-generic .picker"
      fromPicker: ".datepicker-from .picker"
      toPicker: ".datepicker-to .picker"
      resolution: ".resolution"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data


