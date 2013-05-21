require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["line_chart", "bar_chart", "carpet_chart"],
  (LineChart, BarChart, CarpetChart) ->

    exampleUrl = "json?type=rl_ab1&product=210&startTime=2010-12-30&endTime=2010-12-31&resolution=HOURLY"

    drawData = (data) ->
      $("#chart svg").remove()
      console.log JSON.stringify data
      console.log d3.legend
      $("#chart h3").text data.metaData.chartTitle
      $("#legend").text data.allDataLines.map (dl) -> dl.yTitle
      lineChart = LineChart.init(
        parent: "#chart"
        data: data
        scale:
          x:
            type: "date"
      )
      lineChart.draw()

    drawUrl = (url) ->
      $.ajax url,
        success: drawData
        error: (err) ->
          alert err

    $(document).ready ->
      $("#url").keyup (e) ->
        return unless e.which is 13
        drawUrl $("#url").val()

      $("#templates a").click (e) ->
        $("#url").val $(e.target).text()
        drawUrl $(e.target).text()

      $("#url").val exampleUrl
      drawUrl exampleUrl
