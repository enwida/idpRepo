require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["chart_manager", "navigation"],
  (ChartManager, Navigation) ->

    $(document).ready ->
      $(".chart").each ->
        id = $(@).attr "data-chart-id"
        ChartManager.attachTo $(@), id: id

