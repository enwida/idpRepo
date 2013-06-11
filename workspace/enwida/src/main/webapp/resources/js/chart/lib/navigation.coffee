define ->

  class Navigation

    productParts: ["type", "posneg", "timeslot"]
    treeParts: ["type", "timeslot", "posneg"]
    dateFormat: d3.time.format "%Y-%m-%d"
    datePickerFormat: "yyyy-mm-dd"

    constructor: (@element) ->
      @chartId = $(@element).attr "data-chart-id"
      return throw "No chart ID given" unless @chartId

    draw: (parent=@element, callback) ->
      @getNavigationData (err, data) =>
        return callback err if err?
        parent.append @getNavigationElements data
        defaults = @navigationData.defaults
        @setProduct defaults.tsoId, defaults.product
        @element.find(".resolutions").val defaults.resolution
        @setTimeRange defaults.timeRange
        callback? null, data

    getNavigationData: (callback) ->
      $.ajax "navigation.test",
        data: chartId: @chartId
        success: (data) =>
          @navigationData = data
          callback null, data
        error: (err) -> callback err

    getNavigationElements: (navigationData) ->
      console.log navigationData
      root = $ "<div class='navigation'>"
      productSelection = $ "<div class='productselect'>"
      timeSelection = $ "<div class='timeselect'>"
      timeSelection.append @getTimeElement navigationData.timeRangeMax, "from"
      timeSelection.append @getTimeElement navigationData.timeRangeMax, "to"
      productSelection.append @getTsoElement navigationData.tsos
      productSelection.append @getProductElements navigationData.productTrees[0]
      timeSelection.append @getResolutionElement navigationData.allResolutions
      root.append productSelection
      root.append timeSelection
      root

    getTimeElement: (timeRange, klass="time") ->
      result = $("<input type='text' class='#{klass}'>")
      result.datepicker
        format: @datePickerFormat
      result.on "changeDate", (e) =>
        @timeRange[klass] = e.date
      result

    getTsoElement: (tsos) ->
      result = $("<select class='tsos'>")
      for key of tsos
        option = $ "<option value='#{key}'>"
        option.text tsos[key]
        result.append option
      result.change => @updateSelections()
      result

    getProductElements: (productTree) ->
      result = $ "<div class='product'>"
      result.css "display", "inline"
      depth = @getTreeDepth productTree.root
      for name in @productParts
        select = $ "<select class='product-#{name}'>"
        select.change => @updateSelections()
        result.append select
      result

    getResolutionElement: (resolutions) ->
      result = $ "<select class='resolutions'>"
      for resolution in resolutions
        option = $ "<option value='#{resolution}'>"
        option.text resolution
        result.append option
      result.select @navigationData.defaults.resolution
      result

    getTreeDepth: (root) ->
      result = 0
      node = root
      while node.children.length > 0
        result += 1
        node = node.children[0]
      result

    setProduct: (tso, product) ->
      tsoElement = @element.find ".tsos"
      tsoElement.val tso
      productTree = _.find @navigationData.productTrees, (t) -> t.tso is tso
      product = "" + product
      node = productTree.root

      selections = {}
      for i in [0...product.length]
        id = parseInt product.substring i, i + 1
        name = @productParts[i]
        selections[name] = id

      nextNode = null
      for i in [0...product.length]
        nextNode = null
        name = @treeParts[i]
        select = @element.find ".product-#{name}"
        select.empty()
        for child in node.children
          option = $ "<option value='#{child.id}'>"
          option.text child.name
          if child.id is selections[name]
            option.attr "selected", true
            nextNode = child
          select.append option
        node = nextNode ? node.children[0]

    setTimeRange: (timeRange) ->
      @timeRange = timeRange
      @element.find(".from").datepicker "setValue", new Date timeRange.from
      @element.find(".to").datepicker "setValue", new Date timeRange.to

    getProduct: ->
      result = ""
      for name in @productParts
        result += $(@element).find(".product-#{name}").val()
      console.log result
      result

    getTso: ->
      @element.find(".tsos").val()

    getResolution: ->
      @element.find(".resolutions").val()

    getTimeRange: ->
      console.log @timeRange
      @timeRange

    updateSelections: ->
      @setProduct parseInt(@getTso()), @getProduct()

    isDateScale: ->
      @navigationData.isDateScale
