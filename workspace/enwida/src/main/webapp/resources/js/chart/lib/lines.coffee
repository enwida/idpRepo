define ->

  flight.component ->

    addClass = (element, klass) ->
      classes = element.attr "class"
      classes += " #{klass}"
      element.attr "class", classes

    removeClass = (element, klass) ->
      classes = element.attr "class"
      classes = classes.replace new RegExp("\s*#{klass}\s*", "g"), ""
      element.attr "class", classes

    @updateLines = (_, opts) ->
      @$node.empty()
      ul = $("<ul>")
      @$node.append ul
      [0...opts.lines.length].forEach (i) =>
        line = opts.lines[i]
        li = $("<li>").addClass("line#{i}")
          .append($("<table>").attr("cellpadding", "3")
            .append($("<tr>")
              .append($("<td>")
                .append($("<div>").addClass("linesquare")))
              .append($("<td>").text(line.title))))
        li.click =>
          # _ holds the event...
          _ = window._
          if _.contains @attr.disabledLines, i
            @attr.disabledLines = _.without @attr.disabledLines, i
          else
            @attr.disabledLines.push i

          li.toggleClass "hidden"
          @trigger "toggleLine", lineId: i
        li.hover (=>
          # toggleClass does not work with SVG elements
          @$node.closest(".chart").find(".line#{i}").each ->
            addClass $(@), "selected"
          @$node.closest(".chart").find(".dot#{i}").each ->
            addClass $(@), "selected"
        ), (=>
          # toggleClass does not work with SVG elements
          @$node.closest(".chart").find(".line#{i}").each ->
            removeClass $(@), "selected"
          @$node.closest(".chart").find(".dot#{i}").each ->
            removeClass $(@), "selected"
        )
        ul.append li

      for i in @attr.disabledLines
        @$node.find("li.line#{i}").addClass "hidden"
        @trigger "toggleLine", lineId: i, duration: 0

    @after "initialize", ->
      @attr.disabledLines = []
      @on "updateLines", @updateLines
