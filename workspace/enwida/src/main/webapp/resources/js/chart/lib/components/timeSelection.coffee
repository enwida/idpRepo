define ->

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

    @modifyDate = (base, opts, backwards) ->
      result = new Date base

      days = parseInt opts.days
      months = parseInt opts.months
      years = parseInt opts.years

      opts.backwards = backwards if backwards?
      if opts.backwards
        days *= -1
        months *= -1
        years *= -1

      unless isNaN(days)
        result.setDate(result.getDate() + days)
      unless isNaN(months)
        result.setMonth(result.getMonth() + months)
      unless isNaN(years)
        result.setFullYear(result.getFullYear() + years)

      result

    @getDateModifier = (timeRange) ->
      switch timeRange
        when "Day" then days: 1
        when "Week" then days: 7
        when "Month" then months: 1
        when "Year" then years: 1
        else {}

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
          format: @datePickerFormats[timeRange]
          weekStart: 1
          calendarWeeks: timeRange is "Week"
          weekselect: timeRange is "Week"
          viewMode: @viewModes[timeRange]
          minViewMode: @viewModes[timeRange]
          startDate: limits.from ? "1900-01-01"
          endDate: limits.to ? new Date()

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
      @showDatepicker @select("timeRange").val()

    @syncDatepickers = (date, sender) ->
      for timeRange in @timeRanges
        continue if timeRange is sender
        element = @getDatepickerElement timeRange
        newDate = switch sender
          when "Day"
            if timeRange is "Week"
              @getWeekStart date
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

    @getDateFrom = (from) ->
      result = new Date from
      switch @select("timeRange").val()
        when "Week"
          result = @getWeekStart result
        when "Month"
          result.setDate 1
        when "Year"
          result.setMonth 0
          result.setDate 1
      result

    @getDateTo = (from) ->
      @modifyDate from, @getDateModifier @select("timeRange").val()

    @getWeekStart = (date) ->
      result = new Date date
      while result.getDay() isnt 1
        result.setDate (result.getDate() - 1)
      result

    @getDatepickerElement = (timeRange) ->
      @$node.find(".datepicker-#{timeRange} .from")

    @getVisibleFromDate = ->
      timeRange = @select("timeRange").val()
      new Date @$node.find(".datepicker-#{timeRange} .from").data("datepicker").date

    @setVisibleFromDate = (date) ->
      @$node.find(".datepicker-generic:visible .from").datepicker "setDate", date

    @getTimeRange = (timeRange) ->
      diff = timeRange.to - timeRange.from
      if diff < 1000*60*60*24*7 then "Day"
      else if diff < 1000*60*60*24*28 then "Week"
      else if diff < 1000*60*60*24*365 then "Month"
      else "Year"

    @forEachDatepicker = (f) ->
      for timeRange in @timeRanges
        f @getDatepickerElement(timeRange)

    @prevPeriode = ->
      modifier = @getDateModifier(@select("timeRange").val())
      modifier.backwards = true
      date = @modifyDate @getVisibleFromDate(), modifier
      @setVisibleFromDate date

    @nextPeriode = ->
      @setVisibleFromDate @getDateTo @getVisibleFromDate()

    @defaultAttrs
      timeRange: ".timerange"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data


