define ->

  class ProductTree

    constructor: (navigationData) ->
      @trees = {}
      for tree in navigationData.productTrees
        @trees[parseInt tree.tso] = tree

    tree: (tso) -> @trees[parseInt tso]

    traverse: (tso, product) ->
      tree = @tree parseInt tso
      node = tree.root

      sProduct = product + "" # convert to string
      while sProduct.length > 0 and node.children.length > 0
        node = _.find node.children, (child) -> sProduct.indexOf(child.id + "") is 0
        return null unless node?
        sProduct = sProduct.replace node.id + "", ""
      node

    maxDepth: ->
      nodeMaxDepth = (node) ->
        return 0 if not node.children? or node.children.length is 0
        1 + _.max node.children.map (child) -> nodeMaxDepth child

      _.max _.values(@trees).map (tree) -> nodeMaxDepth tree.root

  init: (navigationData) -> new ProductTree navigationData

