define ->

  class ProductTree

    constructor: (navigationData) ->
      @trees = {}
      for tree in navigationData.productTrees
        @trees[parseInt tree.tso] = tree

    tree: (tso) -> @trees[parseInt tso]

    traverse: (tso, product) ->
      tree = @tree tso
      node = tree.root

      sProduct = product + "" # convert to string
      for i in [0...sProduct.length]
        id = parseInt sProduct[i]
        node = _(node.children).find (child) -> child.id is id
      node

  init: (navigationData) -> new ProductTree navigationData

