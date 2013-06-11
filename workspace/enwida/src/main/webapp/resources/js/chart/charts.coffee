require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["line_chart", "bar_chart", "carpet_chart", "chart_manager"],
  (LineChart, BarChart, CarpetChart, ChartManager) ->

    $(document).ready ->
      $(".chart").each ->
        manager = new ChartManager $(@)
        manager.draw()
