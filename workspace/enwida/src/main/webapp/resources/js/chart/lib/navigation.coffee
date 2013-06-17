define ->

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
          .append($("<input>").attr("type", "input").addClass("from"))
          .append($("<input>").attr("type", "input").addClass("to"))
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
        @fillTso()
        @fillProduct()
        @fillResolutions()
        @fillTimeRange()
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

    @fillTimeRange = ->
      @select("from").datepicker
        format: @datePickerFormat
      @select("to").datepicker
        format: @datePickerFormat

    @setDefaults = (defaults) ->
      @select("tso").val defaults.tso
      @setProduct defaults.product
      @select("resolution").val defaults.resolution
      @select("from").datepicker "setValue", new Date defaults.timeRange.from
      @select("to").datepicker "setValue", new Date defaults.timeRange.to

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

    @triggerGetLines = ->
      @trigger "getLines",
        tso: @select("tso").val()
        product: @getProduct()
        timeRange:
          from: @select("from").val()
          to:   @select("to").val()
        resolution: @select("resolution").val()

    @setupEvents = ->
      @$node.select("select").change => @triggerGetLines()
      @select("from").on "changeDate", => @triggerGetLines()
      @select("to").on "changeDate", => @triggerGetLines()

    @defaultAttrs
      tso: ".tso"
      product: ".product"
      resolution: ".resolution"
      from: ".from"
      to: ".to"

    @after "initialize", ->
      @on "refresh", @refresh
      @on "updateProducts", @updateProducts

      @createElements()
      @refresh()
      @setupEvents()
