require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["components/chart_manager", "util/logger"], (ChartManager, Logger) ->

    $(document).ready ->
      # Inject global logger object
      window.Logger = Logger.init "debug"

      # Handle every chart div in the document
      $(".chart").each ->
        id = $(@).attr "data-chart-id"
        ChartManager.attachTo $(@), id: id

