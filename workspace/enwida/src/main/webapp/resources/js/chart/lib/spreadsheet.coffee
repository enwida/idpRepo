define ->

  class Spreadsheet

    constructor: (element, @isDateScale=false) ->
      @element = $ element

    draw: (data) ->
      @element.empty()
      root = $ "<table>"
      root.append @getTableHeader data
      @element.append root
      @addTableRows root, data

    getTableHeader: (data, xLabel="Time") ->
      root = $ "<tr>"
      root.append $("<th>").text xLabel
      for line in data
        console.log line
        root.append $("<th>").text line.title

    addTableRows: (root, data) ->
      console.log "datescale: #{@isDateScale}"
      dateFormat = d3.time.format "%Y-%m-%d %H:%M"
      values = {}
      for line in data
        for dp in line.dataPoints
          values[dp.x] ?= {}
          values[dp.x][line.title] = dp.y

      for x in _(values).keys().sort()
        row = $ "<tr>"
        colX = $ "<td>"
        colX.text if @isDateScale then dateFormat new Date parseInt x else x
        row.append colX
        for line in data
          colY = $ "<td>"
          colY.text values[x][line.title] ? ""
          row.append colY
        root.append row
