define [ "drawable/line_chart"
         "drawable/bar_chart"
         "drawable/carpet_chart"
         "drawable/min_max_chart"
         "drawable/pos_neg_chart"
       ],

  (LineChart
   BarChart
   CarpetChart
   MinMaxChart
   PosNegChart
   ) ->

  flight.component ->

    chartClasses =
      line   : LineChart
      bar    : BarChart
      minmax : MinMaxChart
      posneg : PosNegChart
      carpet : CarpetChart

    @onNavigationData = (_, opts) ->
      @attr.chartOptions.xLabel = opts.data.xAxisLabel
      @attr.chartOptions.yLabel = opts.data.yAxisLabel
      @attr.chartOptions.scale =
        x:
          type: if opts.data.isDateScale then "date" else "linear"

    @getChart = (lines) ->
      @attr.chartOptions.lines = lines
      ChartClass = chartClasses[@attr.type] ? LineChart
      ChartClass.init @attr.chartOptions

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
      @setupTooltips()

    @setupTooltips = ->
      @$node.find("circle").tipsy(gravity: "sw", html: true, opacity: 0.95)
      @$node.find("rect").tipsy(gravity: "sw", html: true, opacity: 0.95)

    @after "initialize", ->
      @on "navigationData", @onNavigationData
      @on "draw", @draw

      @attr.chartOptions =
        parent: @$node
        width: @attr.width
