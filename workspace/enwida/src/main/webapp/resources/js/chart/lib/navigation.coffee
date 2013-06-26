define ["resolution"], (Resolution) ->

  flight.component ->

    @productParts     = ["type", "posneg", "timeslot"]
    @treeParts        = ["type", "timeslot", "posneg"]
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
      productSelect =
        $("<div>").addClass("productSelect")
          .append($("<select>").addClass("tso")
            .change => @trigger "updateProducts")

      product = $("<div>").addClass("product").css("display", "inline")
      for name in @productParts
        product.append($("<select>").addClass(name)
          .change => @trigger "updateProducts")
      productSelect.append product

      timeSelect = $("<div>").addClass("timeselect")

      for timeRange in @timeRanges
        timeSelect.append($("<div>").addClass("input-append")
          .addClass("datepicker-generic").addClass("datepicker-#{timeRange}")
          .append($("<input>").attr("type", "text").addClass("from").attr("readonly", true))
          .append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon")
            .append($("<i>").addClass("icon-th"))).hide())

      timeSelect.append($("<select>").addClass("timerange")).change => @refreshDatepicker()

      @$node.append productSelect
      @$node.append timeSelect

    @getNavigationData = (callback) ->
      $.ajax "navigation.test",
        data: chartId: @attr.id
        success: (data) =>
          @navigationData = data
          callback null, data
        error: (err) -> callback err

    @refresh = ->
      @getNavigationData (err, data) =>
        throw err if err?
        dateLimits =
          from : new Date data.timeRangeMax.from
          to   : new Date data.timeRangeMax.to

        @fillTso()
        @fillProduct()
        @fillResolutions()
        @fillTimeRange dateLimits
        @setDefaults data.defaults
        @trigger "updateNavigation", data: data
        @triggerGetLines()

    @fillTso = ->
      element = @select("tso")
      element.empty()

      tsos = @navigationData.tsos
      for key of @navigationData.tsos
        element.append($("<option>").val(key).text(tsos[key]))

    @fillProduct = ->
      @setProduct 111

    @fillResolutions = ->
      element = @select "resolution"
      element.empty()
      for r in @navigationData.allResolutions
        element.append($("<option>").val(r).text(r))

    @fillTimeRange = (limits={}) ->
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
          @updateDatepickers e.date, timeRange

      timeRange = @select("timeRange")
      for tr in @timeRanges
        # TODO: locale
        timeRange.append($("<option>").val(tr).text(tr))

      @refreshDatepicker()

    @refreshDatepicker = ->
      timeRange = @select("timeRange").val()
      @$node.find(".datepicker-generic").hide()
      @$node.find(".datepicker-#{timeRange}").show()

    @updateDatepickers = (date, sender) ->
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

    @setDefaults = (defaults) ->
      @select("tso").val defaults.tso
      @setProduct defaults.product
      @select("resolution").val defaults.resolution
      @select("timeRange").val @getTimeRange defaults.timeRange
      @refreshDatepicker()
      @forEachDatepicker (picker) ->
        picker.datepicker "setDate", new Date defaults.timeRange.from

    @getProductTree = ->
      tso = parseInt @select("tso").val()
      _.find @navigationData.productTrees, (t) -> t.tso is tso

    @setProduct = (product) ->
      product = "" + product # convert to string
      values = {}
      for i in [0...@productParts.length]
        id = parseInt product.substring i, i + 1
        values[@productParts[i]] = id

      node = @getProductTree().root
      for name in @treeParts
        element = @select("product").find ".#{name}"
        element.empty()
        for child in node.children
          element.append $("<option>").val(child.id).text(child.name)

        id = values[name]
        element.val id
        node = (_.find node.children, (c) -> c.id is id) ? node.children[0]

    @getProduct = ->
      result = ""
      for name in @productParts
        element = @select("product").find(".#{name}")
        result += element.val()
      result

    @updateProducts = ->
      @setProduct @getProduct()

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
      result = new Date from
      switch @select("timeRange").val()
        when "Day"
          result.setDate(result.getDate() + 1)
        when "Week"
          result.setDate(result.getDate() + 7)
        when "Month"
          result.setMonth(result.getMonth() + 1)
        when "Year"
          result.setFullYear(result.getFullYear() + 1)
      result

    @getWeekStart = (date) ->
      result = new Date date
      while result.getDay() isnt 1
        result.setDate (result.getDate() - 1)
      result

    @getDatepickerElement = (timeRange) ->
      @$node.find(".datepicker-#{timeRange} .from")

    @getVisibleFromDate = ->
      new Date @$node.find(".datepicker-generic:visible .from").data("datepicker").date

    @getTimeRange = (timeRange) ->
      diff = timeRange.to - timeRange.from
      if diff < 1000*60*60*24*7 then "Day"
      else if diff < 1000*60*60*24*28 then "Week"
      else if diff < 1000*60*60*24*365 then "Month"
      else "Year"

    @forEachDatepicker = (f) ->
      for timeRange in @timeRanges
        f @getDatepickerElement(timeRange)

    @triggerGetLines = (opts={}) ->
      from = @getDateFrom(opts.from ? @getVisibleFromDate())
      timeRange =
        from: from
        to:   @getDateTo from

      resolution = Resolution.getOptimalResolution timeRange,
          @navigationData.allResolutions,
          @attr.width

      @trigger "getLines",
        tso: @select("tso").val()
        product: @getProduct()
        timeRange: timeRange
        resolution: resolution
        # resolution: @select("resolution").val()

    @setupEvents = ->
      @$node.on "change", (e) =>
        @triggerGetLines()

    @defaultAttrs
      tso: ".tso"
      product: ".product"
      resolution: ".resolution"
      timeRange: ".timerange"

    @after "initialize", ->
      @on "refresh", @refresh
      @on "updateProducts", @updateProducts

      @createElements()
      @refresh()
      @setupEvents()
