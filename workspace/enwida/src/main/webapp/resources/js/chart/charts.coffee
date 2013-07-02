require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["components/chart_manager"], (ChartManager) ->

    $(document).ready ->
      $(".chart").each ->
        id = $(@).attr "data-chart-id"
        ChartManager.attachTo $(@), id: id

