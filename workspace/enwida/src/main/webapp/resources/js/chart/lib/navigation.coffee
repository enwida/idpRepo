define ->

  options:
    parent: "#chart"

  init: (options, callback) ->
    $.extend @options, options
    @chartId = $(options.parent).attr "data-chart-id"
    throw "No data-chart-id attribute" unless @chartId?

  getNavigationData: (callback) ->
    $.ajax "navigation",
      data: id: @chartId
      success: callback
