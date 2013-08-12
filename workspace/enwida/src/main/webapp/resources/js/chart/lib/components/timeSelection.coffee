define ["../util/time_utils"], (TimeUtils) ->

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

    @createElements = ->
      @$node.empty()
      for timeRange in @timeRanges
        @$node.append($("<div>").addClass("input-append")
          .addClass("datepicker-generic").addClass("datepicker-#{timeRange}")
          .append($("<input>").attr("type", "text").addClass("from").attr("readonly", true))
          .append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon")
            .append($("<i>").addClass("icon-th"))).hide())

      @$node.append($("<div>").addClass("prevPeriode")
        .append($("<i>").addClass("icon-chevron-left"))
        .click => @prevPeriode())

      @$node.append($("<select>").addClass("timerange")).change => @showCurrentDatepicker()

      @$node.append($("<div>").addClass("nextPeriode")
        .append($("<i>").addClass("icon-chevron-right"))
        .click => @nextPeriode())

    @refresh = (data) ->
      @attr.navigationData = data
      @createElements()

      dateLimits =
        from : new Date data.timeRangeMax.from
        to   : new Date data.timeRangeMax.to

      @fillTimeRange()
      @fillDatePickers dateLimits

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

        element.closest(".datepicker-generic").find(".fromIcon").click (e) =>
          $(e.target).closest(".datepicker-generic").find(".from").datepicker "show"

        # Keep dates in sync
        element.on "changeDate", (e) =>
          @syncDatepickers e.date, timeRange

      @showCurrentDatepicker()

    @showDatepicker = (timeRange) ->
      @$node.find(".datepicker-generic").hide()
      @$node.find(".datepicker-#{timeRange}").show()

    @showCurrentDatepicker = ->
      @showDatepicker @getCurrentTimeRange()

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
      @syncDatepickers date, @getCurrentTimeRange()

    @prevPeriode = ->
      modifier = TimeUtils.getDateModifier @getCurrentTimeRange()
      modifier.backwards = true
      date = TimeUtils.modifyDate @getFromDate(), modifier
      @setFromDate date

    @nextPeriode = ->
      time = TimeUtils.getDateTo @getFromDate(), @getCurrentTimeRange()
      @setFromDate time

    @getCurrentTimeRange = ->
      @select("timeRange").val()

    @defaultAttrs
      timeRange: ".timerange"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data


