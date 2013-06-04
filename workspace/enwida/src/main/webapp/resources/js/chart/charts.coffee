require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["line_chart", "bar_chart", "carpet_chart"],
  (LineChart, BarChart, CarpetChart) ->

    exampleUrl = "lines?type=rl_ab1&product=210&startTime=2010-12-30&endTime=2010-12-31&resolution=HOURLY"

    chartOptions =
      parent: "#chart"

    drawNavigation = (data, callback) ->
      chart_id = $("#chart").attr "data-chart-id"

      $.ajax "navigation?id=#{chart_id}",
        error: (err) -> alert err.message
        success: (data) ->
          $("#chart h3").text data.title
          chartOptions.width = data.width
          chartOptions.height = data.height
          # FIXME: read from navigation info
          chartOptions.scale =
            x:
              type: "date"
          callback?()

    drawData = (data) ->
      console.log data
      $("#chart svg").remove()
      chartOptions.lines = [data]

      lineChart = LineChart.init chartOptions
      lineChart.draw()
      $("circle").tipsy(gravity: "s")

    drawUrl = (url) ->
      $.ajax url,
        success: drawData
        error: (err) -> alert err.message

    $(document).ready ->
      $("#url").keyup (e) ->
        return unless e.which is 13
        drawUrl $("#url").val()

      $("#templates a").click (e) ->
        $("#url").val $(e.target).text()
        drawUrl $(e.target).text()

      $("#url").val exampleUrl
      # drawNavigation()
      drawUrl exampleUrl
