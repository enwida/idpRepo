define ->

  flight.component ->

    @updateLines = (_, opts) ->
      @$node.empty()
      ul = $("<ul>")
      @$node.append ul
      [0...opts.lines.length].forEach (i) =>
        line = opts.lines[i]
        li = $("<li>").addClass("line#{i}")
          .append($("<span>").text(line.title))
        li.click =>
            li.toggleClass "hidden"
            @trigger "toggleLine", lineId: i
        li.hover (-> li.toggleClass "selected"), (-> li.toggleClass "selected")
        ul.append li

    @after "initialize", ->
      @on "updateLines", @updateLines
