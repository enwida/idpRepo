$(document).ready ->
  setupTableOfContents()

setupTableOfContents = ->
  roots = [$(".toc")]
  findParent = (nest) ->
    return roots[nest - 1] if roots[nest - 1]?
    findParent(nest - 1)

  invalidateParents = (nest) ->
    roots[i] = null for i in [(nest + 1)..8]

  $(".content").find("h1,h2,h3,h4,h5,h6,h7,h8").each ->
    element = $(@)
    anchorName = getAnchorName(element.text())
    element.attr("id", anchorName)

    nest = getHeadlineNest element
    ul = roots[nest]
    unless ul?
      ul = $("<ul>")
      roots[nest] = ul
      parent = findParent nest
      parent.append ul

    ul.append($("<li>")
      .append($("<a>").attr("href", "##{anchorName}")
        .text(element.text())))

    invalidateParents nest

getHeadlineNest = (element) ->
  parseInt element.prop("tagName").substr(1)

getAnchorName = (title) ->
  title.replace(/\s|\//g, "-")

