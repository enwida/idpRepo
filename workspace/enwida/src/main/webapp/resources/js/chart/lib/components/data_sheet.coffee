define ->

  flight.component ->

    @createElements = ->
      @$node.empty()
      @$node.append($("<table>"))

    @refresh = (lines, navigationData) ->
      table = @select "table"
      table.empty()

      # Merge lines
      data = {}
      for line in lines
        for dp in line.dataPoints
          data[dp.x] ?= []
          data[dp.x].push dp.y

      # Header row
      tr = $("<tr>").addClass("header")
      tr.append $("<td>").text navigationData.xAxisLabel
      for line in lines
        tr.append $("<td>").text line.title
      table.append tr

      # Data rows
      oddRow = true
      for x in _(data).keys().sort()
        tr = $("<tr>").addClass if oddRow then "odd" else "even"
        tr.append $("<td>").text \
          if navigationData.isDateScale
            @attr.dateFormat new Date parseInt x
          else x

        for y in data[x]
          tr.append $("<td>").text y

        table.append tr
        oddRow = not oddRow

    @defaultAttrs
      dateFormat: d3.time.format "%Y-%m-%d %H:%M"
      table: "table"

    @after "initialize", ->
      @on "refresh", (_, opts) => @refresh opts.lines, opts.navigationData
      @createElements()

