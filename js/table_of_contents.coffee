# Author: Daniel Strittmatter
# License: MIT

$(document).ready ->
  setupTableOfContents ".toc", "h1,h2,h3,h4,h5,h6,h7,h8"
  setupTableOfContents ".sidebar", "h1,h2,h3", ["nav", "nav-stacked"]
  $('body').scrollspy({ target: '.sidebar' })

setupTableOfContents = (parent, selector, klasses=[]) ->
  roots = [$(parent)]
  findParent = (nest) ->
    return roots[0]        if nest <= 0
    return roots[nest - 1] if roots[nest - 1]?
    findParent(nest - 1)

  invalidateParents = (nest) ->
    roots[i] = null for i in [(nest + 1)..8]

  $(".content").find(selector).each ->
    element = $(@)
    anchorName = getAnchorName(element.text())
    element.attr("id", anchorName)

    nest = getHeadlineNest element
    ul = roots[nest]
    unless ul?
      ul = $("<ul>")
      ul.addClass klass for klass in klasses
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

