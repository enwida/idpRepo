require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["util/logger", "components/chart_manager"], (Logger, ChartManager) ->

    $(document).ready ->
      # Inject global logger object
      window.Logger = Logger.init "debug"

      # Handle every chart div in the document
      $(".chart").each ->
        id = $(@).attr "data-chart-id"
        ChartManager.attachTo $(@), id: id

