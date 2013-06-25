define ["resolution"], (Resolution) ->

  flight.component ->

    @productParts     = ["type", "posneg", "timeslot"]
    @treeParts        = ["type", "timeslot", "posneg"]
    @dateFormat       = d3.time.format "%Y-%m-%d"
    @datePickerFormat = "yyyy-mm-dd"

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
          .append($("<div>").addClass("input-append")
            .append($("<input>").attr("type", "text").addClass("to").attr("readonly", true))
            .append($("<span>").addClass("add-on").addClass("calendar").addClass("toIcon")
              .append($("<i>").addClass("icon-th"))))
          .append($("<select>").addClass("resolution"))

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

        viewMode = data.datepickerViewMode ? "months"

        @fillTso()
        @fillProduct()
        @fillResolutions()
        @fillTimeRange viewMode, dateLimits
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
      @select("to").datepicker
        format: @datePickerFormat
        viewMode: viewMode
        minViewMode: viewMode
        startDate: limits.from ? "1900-01-01"
        endDate: limits.to ? new Date()

      @select("fromIcon").click =>
        console.log @select("from")
        @select("from").datepicker "show"
      @select("toIcon").click =>
        @select("to").datepicker "show"

    @setDefaults = (defaults) ->
      @select("tso").val defaults.tso
      @setProduct defaults.product
      @select("resolution").val defaults.resolution
      @select("from").datepicker "update", new Date defaults.timeRange.from
      @select("to").datepicker "update", new Date defaults.timeRange.to

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

    @triggerGetLines = (opts={}) ->
      timeRange =
        from: opts.from ? new Date @select("from").val()
        to:   opts.to   ? new Date @select("to").val()

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
      from: ".from"
      to: ".to"
      fromIcon: ".fromIcon"
      toIcon: ".toIcon"

    @after "initialize", ->
      @on "refresh", @refresh
      @on "updateProducts", @updateProducts

      @createElements()
      @refresh()
      @setupEvents()
