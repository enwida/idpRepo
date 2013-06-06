require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["line_chart", "bar_chart", "carpet_chart"],
  (LineChart, BarChart, CarpetChart) ->

    drawDefaultChart = (element, chartId, navigationData) ->
      defaults = navigationData.defaults
      getLines chartId,
               defaults.product,
               defaults.tsoId,
               defaults.timeRange.from,
               defaults.timeRange.to,
               defaults.resolution,
               (err, data) ->
                 return console.log err if err?
                 console.log data
                 drawData data

    getNavigationData = (chartId, callback) ->
      $.ajax "navigation.test",
        data:
          chartId: chartId
        success: (data) -> callback null, data
        error: (err) -> callback err

    getLines = (chartId, product, tso, from, to, resolution, callback) ->
      format = d3.time.format "%Y-%m-%d"
      $.ajax "lines.test",
        data:
          chartId: chartId
          product: product
          tso: tso
          startTime: format new Date from
          endTime: format new Date to
          resolution: resolution
        success: (data) -> callback null, data
        error: (err) -> callback err

    drawData = (data, isDateScale=true) ->
      console.log data
      $("#chart svg").remove()
      chartOptions = {}
      chartOptions.scale = x: type: if isDateScale then "date" else "linear"
      chartOptions.lines = data

      lineChart = LineChart.init chartOptions
      lineChart.draw()
      $("circle").tipsy(gravity: "s")

    $(document).ready ->
      $(".chart").each ->
        chartId = $(@).attr "data-chart-id"
        getNavigationData chartId, (err, data) =>
          return console.log err if err?
          console.log data
          drawDefaultChart $(@), chartId, data
