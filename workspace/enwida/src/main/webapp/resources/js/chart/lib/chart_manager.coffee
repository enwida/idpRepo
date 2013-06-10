define ["line_chart", "bar_chart", "carpet_chart", "navigation"],
  (LineChart, BarChart, CarpetChart, Navigation) ->

    class ChartManager

      chartOptions: {}

      constructor: (@element) ->
        @chartId = $(@element).attr "data-chart-id"
        throw "No ID given!" unless @chartId

      draw: ->
        $(@element).empty()
        @navigation = new Navigation @element
        @navigation.draw (err, data) =>
          throw err if err?
          @element.find(".navigation select").change => @drawLines()
          submit = $ "<input type='button' value='test'>"
          submit.click => @drawLines()
          @element.find(".navigation").append submit
          @chartOptions.xLabel = data.xAxisLabel
          @chartOptions.yLabel = data.yAxisLabel
          @drawDefaultChart data

      drawDefaultChart: (navigationData) ->
        defaults = navigationData.defaults
        @getLines defaults.product,
                  defaults.tsoId,
                  defaults.timeRange.from,
                  defaults.timeRange.to,
                  defaults.resolution,
                  (err, data) =>
                    return console.log err if err?
                    console.log data
                    @drawData data

      getLines: (product, tso, from, to, resolution, callback) ->
        format = d3.time.format "%Y-%m-%d"
        $.ajax "lines.test",
          data:
            chartId: @chartId
            product: product
            tso: tso
            startTime: format new Date from
            endTime: format new Date to
            resolution: resolution
          success: (data) -> callback null, data
          error: (err) -> callback err

      drawLines: (callback) ->
        timeRange = @navigation.getTimeRange()
        @getLines @navigation.getProduct(),
                  @navigation.getTso(),
                  timeRange.from,
                  timeRange.to,
                  @navigation.getResolution()
                  (err, data) =>
                    throw err if err?
                    @drawData data
                    callback?()

      drawData: (data, isDateScale=true) ->
        console.log data
        $(@element).find("svg").remove()
        @chartOptions.parent = ".chart[data-chart-id='#{@chartId}']"
        @chartOptions.scale = x: type: if isDateScale then "date" else "linear"
        @chartOptions.lines = data

        lineChart = LineChart.init @chartOptions
        lineChart.draw()
        $("circle").tipsy(gravity: "s")
