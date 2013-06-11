define ["line_chart", "bar_chart", "carpet_chart", "navigation", "spreadsheet"],
  (LineChart, BarChart, CarpetChart, Navigation, Spreadsheet) ->

    class ChartManager

      chartOptions: {}

      constructor: (@element) ->
        @chartId = $(@element).attr "data-chart-id"
        @type = $(@element).attr "data-chart-type" ? "line"
        throw "No ID given!" unless @chartId

      draw: ->
        $(@element).empty()
        metaElement = $ "<div class='chartmeta'>"
        spreadsheetElement = $ "<div class='spreadsheet'>"
        @element.append $ "<div class='visual'>"
        @element.append metaElement
        @element.append spreadsheetElement
        @spreadsheet = new Spreadsheet spreadsheetElement

        @navigation = new Navigation @element
        @navigation.draw metaElement, (err, data) =>
          throw err if err?
          # Draw the heading
          heading = $ "<h2>"
          heading.text data.title
          @element.prepend heading

          # Event handlers
          @element.find(".navigation select").change => @drawLines()
          @element.find(".from").on "changeDate", => @drawLines()
          @element.find(".to").on "changeDate", => @drawLines()

          # Add spreadsheet toggle button
          @spreadsheet.isDateScale = data.isDateScale
          spreadsheetToggle = $ "<input type='button' value='Show data'>"
          spreadsheetToggle.click => @element.find(".spreadsheet").toggle()
          @element.append spreadsheetToggle

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

      drawData: (data) ->
        console.log data
        $(@element).find("svg").remove()
        chart = @getChart data, @type
        chart.draw()
        $("circle").tipsy(gravity: "s")
        @spreadsheet.draw data

      getChart: (data, type="line") ->
        @chartOptions.parent = ".chart[data-chart-id='#{@chartId}'] .visual"
        @chartOptions.lines = data
        switch type
          when "line"
            @chartOptions.scale = x: type: if @navigation.isDateScale() then "date" else "linear"
            LineChart.init @chartOptions
          when "carpet"
            @chartOptions.scale =
              x:
                type: "ordinal"
                padding: 0
            @chartOptions.lines = @toCarpet data
            CarpetChart.init @chartOptions

      toCarpet: (data) ->
        data[0].dataPoints.map (dp) ->
          day = new Date dp.x
          day = day.getMonth() + "/" + day.getDate()
          hour = new Date dp.x
          hour = hour.getHours()
          value = dp.y
          dp.x = day
          dp.y = hour
          dp.v = value
          dp
        data
