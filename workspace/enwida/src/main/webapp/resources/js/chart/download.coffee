require.config
  baseUrl: "/enwida/resources/js/chart/lib"

require ["util/logger", "components/download_manager"], (Logger, DownloadManager) ->

    $(document).ready ->
      # Inject global logger object
      window.Logger = Logger.init "debug"

      # Create div and attach download manager
      id = parseInt window.location.search.replace("?chartId=", "")
      DownloadManager.attachTo $(".download"), id: id

