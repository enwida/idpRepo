define ->

  flight.component ->

    @productParts     = ["type", "posneg", "timeslot"]

    @createElements = ->
      @$node.empty()
      @$node.append($("<select>").addClass("tso")
        .change => @trigger "updateProducts")

      product = $("<div>").addClass("product").css("display", "inline")
      for name in @productParts
        product.append($("<select>").addClass(name)
          .change => @trigger "updateProducts")
      @$node.append product

    @refresh = (data) ->
      @attr.navigationData = data
      @createElements()
      @fillTso()
      @fillProduct()

    @fillTso = ->
      element = @select("tso")
      tsos = @attr.navigationData.tsos
      for key of tsos
        tsoName = tsos[key]
        element.append($("<option>").val(key).text(tsoName))

    @fillProduct = ->
      @setProduct 111

    @setProduct = (product) ->
      product = "" + product # convert to string
      values = {}
      for i in [0...@productParts.length]
        id = parseInt product.substring i, i + 1
        values[@productParts[i]] = id

      node = @getProductTree().root
      for name, i in @productParts
        element = @select("product").find ".#{name}"
        element.empty()
        for child in node.children
          element.append $("<option>").val(child.id).text(child.name)

        if node.children.length <= 1
          element.hide()
        else
          element.show()

        id = values[name]
        element.val id
        node = (_.find node.children, (c) -> c.id is id) ? node.children[0]

      # Apply restrictions of leaf node
      # TODO

    @getProduct = ->
      result = ""
      for name in @productParts
        element = @select("product").find(".#{name}")
        result += element.val()
      result

    @getProductTree = ->
      tso = @select("tso").val()
      result = _(@attr.navigationData.productTrees).find (tree) -> tree.tso is tso
      result ? @attr.navigationData.productTrees[0]

    @defaultAttrs
      tso: ".tso"
      product: ".product"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.data
      @on "updateProducts", (_, opts) => @refresh opts.data
      
