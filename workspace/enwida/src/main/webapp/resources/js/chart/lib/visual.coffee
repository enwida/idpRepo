define ["line_chart", "carpet_chart"], (LineChart, CarpetChart) ->

  flight.component ->

    @toCarpet = (data) ->
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

    @onNavigationData = (_, opts) ->
      @attr.chartOptions.xLabel = opts.data.xAxisLabel
      @attr.chartOptions.yLabel = opts.data.yAxisLabel
      @attr.chartOptions.scale =
        x:
          type: if opts.data.isDateScale then "date" else "linear"

    @getChart = (lines) ->
      @attr.chartOptions.lines = lines
      switch @attr.type
        when "line"
          LineChart.init @attr.chartOptions
        when "carpet"
          @attr.chartOptions.lines = [@toCarpet(lines)[0]]
          @attr.chartOptions.scale.x.type = "ordinal"
          @attr.chartOptions.scale.x.padding = 0
          CarpetChart.init @attr.chartOptions

    @draw = (_, opts) ->
      @$node.empty()
      chart = @getChart opts.data
      chart.draw()
      @$node.find("circle").tipsy(gravity: "sw", html: true, opacity: 0.95)

    @defaultAttrs
      ChartClass: LineChart

    @after "initialize", ->
      @on "navigationData", @onNavigationData
      @on "draw", @draw

      @attr.chartOptions =
        parent: ".chart[data-chart-id='#{@attr.id}'] .visual"
        width: @attr.width