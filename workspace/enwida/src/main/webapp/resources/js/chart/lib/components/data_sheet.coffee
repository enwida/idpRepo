define ["../util/time_utils"], (TimeUtils) ->

  flight.component ->

    @createElements = ->
      @$node.empty()
      @$node.append($("<table>"))

    @refresh = (lines, navigationData) ->
      table = @select "table"
      table.empty()

      resolution = lines[0].lineRequest.resolution
      dateFormat = TimeUtils.resolutionDateFormat[resolution]

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
        tr.append $("<td>").text line.title + " [" + line.unit + "]"
      table.append tr

      # Data rows
      oddRow = true
      for x in _(data).keys().sort()
        tr = $("<tr>").addClass if oddRow then "odd" else "even"
        tr.append $("<td>").text \
          if navigationData.options.isDateScale
            dateFormat new Date parseInt x
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

