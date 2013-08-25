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

    @updateLines = (e, opts) ->
      @$node.empty()
      ul = $("<ul>")
      @$node.append ul
      [0...opts.lines.length].forEach (i) =>
        color = @$node.closest(".chart,.download").find("svg .line#{i}").css("stroke")
        line = opts.lines[i]
        li = $("<li>").addClass("line#{i}")
          .append($("<table>").attr("cellpadding", "3")
            .append($("<tr>")
              .append($("<td>")
                .append($("<div>")
                  .addClass("linesquare")
                  .css("background-color", color)))
              .append($("<td>").text(line.title))))
        li.click =>
          if _.contains @attr.disabledLines, i
            @attr.disabledLines = _.without @attr.disabledLines, i
          else
            @attr.disabledLines.push i

          li.toggleClass "hidden"
          @trigger "toggleLine", disabledLines: @attr.disabledLines
        li.hover (=>
          # toggleClass does not work with SVG elements
          @$node.closest(".chart").find(".line#{i}").each ->
            addClass $(@), "selected"
            $(@).show()
          @$node.closest(".chart").find(".dot#{i}").each ->
            addClass $(@), "selected"
            $(@).show()
        ), (=>
          # toggleClass does not work with SVG elements
          @$node.closest(".chart").find(".line#{i}").each ->
            removeClass $(@), "selected"
          @$node.closest(".chart").find(".dot#{i}").each ->
            removeClass $(@), "selected"

          if li.hasClass("hidden")
            @$node.closest(".chart").find(".visual .line#{i}").hide()
            @$node.closest(".chart").find(".visual .dot#{i}").hide()
        )
        ul.append li

      @trigger "toggleLine", disabledLines: @attr.disabledLines
      for i in @attr.disabledLines
        @$node.find("li.line#{i}").addClass "hidden"

    @after "initialize", ->
      @attr.disabledLines = []
      @on "updateLines", @updateLines
      @on "disabledLines", (_, opts) ->
        @attr.disabledLines = opts.lines if opts.lines?

