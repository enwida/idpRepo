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

      timeSelect =
        $("<div>").addClass("timeselect")
          .append($("<div>").addClass("input-append")
            .append($("<input>").attr("type", "text").addClass("from").attr("readonly", true))
            .append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon")
              .append($("<i>").addClass("icon-th"))))
          .append($("<select>").addClass("timerange")).change => @refreshDatepicker()

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
        @fillTimeRange "days", dateLimits
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

    @fillTimeRange = (viewMode="days", limits={}) ->
      @select("from").datepicker
        format: @datePickerFormat
        viewMode: viewMode
        minViewMode: viewMode
        startDate: limits.from ? "1900-01-01"
        endDate: limits.to ? new Date()

      @select("fromIcon").click =>
        @select("from").datepicker "show"

      timeRange = @select("timeRange")
      for tr in @timeRanges
        # TODO: locale
        timeRange.append($("<option>").val(tr).text(tr))

    @refreshDatepicker = ->
      switch @select("timeRange").val()
        when "Day"
          @setDateRangeViewMode 0
        when "Week"
          @setDateRangeViewMode 0
        when "Month"
          @setDateRangeViewMode 1
        when "Year"
          @setDateRangeViewMode 2

    @setDateRangeViewMode = (mode) ->
      console.log "Settings mode to #{mode}"
      @select("from").data("datepicker").minViewMode = mode
      @select("from").data("datepicker").startViewMode = mode
      @select("from").data("datepicker").viewMode = mode
      @select("from").datepicker("show")
      @select("from").datepicker("hide")

    @setDefaults = (defaults) ->
      @select("tso").val defaults.tso
      @setProduct defaults.product
      @select("resolution").val defaults.resolution
      @select("from").datepicker "update", new Date defaults.timeRange.from

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

    @triggerGetLines = (opts={}) ->
      from = opts.from ? new Date @select("from").val()
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
      @$node.select("select").change => @triggerGetLines()
      @select("from").on "changeDate", (e) => @triggerGetLines from: e.date

    @defaultAttrs
      tso: ".tso"
      product: ".product"
      resolution: ".resolution"
      from: ".from"
      fromIcon: ".fromIcon"
      timeRange: ".timerange"

    @after "initialize", ->
      @on "refresh", @refresh
      @on "updateProducts", @updateProducts

      @createElements()
      @refresh()
      @setupEvents()
