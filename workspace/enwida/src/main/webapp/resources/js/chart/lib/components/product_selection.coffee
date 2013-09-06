define ["util/product_tree"], (ProductTree) ->

  flight.component ->

    @refresh = (data) ->
      @attr.navigationData = data
      @attr.tree = ProductTree.init data
      @attr.treeDepth = @attr.tree.maxDepth()

      @createElements()
      @setupEvents()

      @fillTso()

      # Apply defaults
      @select("tso").val data.defaults.tsoId
      @setProduct data.defaults.product
      @select("tso").change()

    @createElements = ->
      @$node.empty()
      @$node.append $("<select>").addClass("tso")
      @$node.append $("<div>").addClass("product").css("display", "inline")
      @createProductElements @attr.treeDepth

    @createProductElements = (n) ->
      product = @select "product"
      for i in [0...n]
        product.append $("<select>").addClass("product-#{i}")

    @setupEvents = ->
      @$node.find("select").change =>
        tso = @select("tso").val()
        product = @normalizeProduct()
        @setProduct product
        @trigger "productSelectionChanged", tso: tso, product: product

    @fillTso = ->
      element = @select("tso")
      tsos = @attr.navigationData.tsos
      for key of tsos
        tsoName = tsos[key]
        element.append($("<option>").val(key).text(tsoName))

    @setProduct = (product) ->
      product = "" + product # convert to string
      node = @getProductTree().root
      level = 0

      while product.length > 0 and node.children.length > 0
        # Fill selection box of current tree level
        element = @select("product").find ".product-#{level}"
        element.empty()
        for child in node.children
          element.append $("<option>").val(child.id).text(child.name)

        # Apply visibility of selection box
        if node.children.length <= 1
          element.hide()
        else
          element.show()

        # Find matching child
        child = ProductTree.matchingChild product, node
        # Use first child in case of no match
        child ?= node.children[0]
        # Update product string
        product = product.replace child.id + "", ""
        # Set child value
        element.val child.id
        node = child
        level++

      @attr.currentLevel = level
      for i in [level...@attr.treeDepth]
        @$node.find(".product-#{i}").hide()

    @getProduct = ->
      tso = @select("tso").val()
      node = @getProductTree().root
      result = ""
      for i in [0...@attr.treeDepth]
        # Get value of current element
        element = @select("product").find(".product-#{i}")
        val = element.val()

        # Try to traverse the tree
        testNode = @attr.tree.traverse tso, result + val
        # Take the safe way if no node was reached
        val = node.children[0].id + "" unless testNode?

        # Update result and current node
        result += val
        node = @attr.tree.traverse tso, result

        # Return early if we reached a leaf
        return result if node.children.length is 0

      result
    
    @normalizeProduct = ->
      product = @getProduct()
      @setProduct product
      @getProduct()

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
      

