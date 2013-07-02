define ["line_chart", "bar_chart", "carpet_chart", "min_max_chart", "pos_neg_chart"],
(LineChart, BarChart, CarpetChart, MinMaxChart, PosNegChart) ->

  flight.component ->

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
        when "bar"
          BarChart.init @attr.chartOptions
        when "minmax"
          MinMaxChart.init @attr.chartOptions
        when "carpet"
          CarpetChart.init @attr.chartOptions
        when "posneg"
          PosNegChart.init @attr.chartOptions
        else
          console.log "Unknown chart type: '#{@attr.type}'"

    @hideDisabledLines = (disabledLines) ->
      for lineId in disabledLines
        @$node.find(".line#{lineId}").hide()
        @$node.find(".dot#{lineId}").hide()

    @draw = (_, opts) ->
      @$node.empty()
      @attr.chartOptions.disabledLines = opts.disabledLines
      chart = @getChart opts.data
      chart.draw()
      @hideDisabledLines opts.disabledLines

      switch @attr.type
        when "line"
          @$node.find("circle").tipsy(gravity: "sw", html: true, opacity: 0.95)
        when "bar"
          @$node.find("rect").tipsy(gravity: "sw", html: true, opacity: 0.95)
        when "minmax"
          @$node.find("circle").tipsy(gravity: "sw", html: true, opacity: 0.95)
          @$node.find("rect").tipsy(gravity: "sw", html: true, opacity: 0.95)
        when "carpet"
          @$node.find("rect").tipsy(gravity: "sw", html: true, opacity: 0.95)
        when "posneg"
          @$node.find("rect").tipsy(gravity: "sw", html: true, opacity: 0.95)

    @defaultAttrs
      ChartClass: LineChart

    @after "initialize", ->
      @on "navigationData", @onNavigationData
      @on "draw", @draw

      @attr.chartOptions =
        parent: @$node
        width: @attr.width
