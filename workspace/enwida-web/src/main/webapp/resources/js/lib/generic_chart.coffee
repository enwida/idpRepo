define ["scale"], (scale) ->

  class Chart

    constructor: (options) ->
      throw "No data given" unless options? and options.data?

      default_options =
        margin:
          top: 20
          left: 50
          right: 20
          bottom: 30
        width: 960
        height: 500
        parent: "body"

      @options = $.extend default_options, options
      @data = options.data

      # @data is an array of line data
      @data = [@data] unless @data[0] instanceof Array
      scale.init @
      @generateAxes()

    generateXAxis: ->
      @xAxis = d3.svg.axis().scale(@xScale).orient("bottom")

    generateYAxis: ->
      @yAxis = d3.svg.axis().scale(@yScale).orient("left")

    generateAxes: (xScale, yScale) ->
      @generateXAxis()
      @generateYAxis()

    drawSvg: ->
      return if @svg?
      width = @options.width + @options.margin.left + @options.margin.right
      height = @options.height + @options.margin.top + @options.margin.bottom
      @svg = d3.select(@options.parent).append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
          .attr("transform", "translate(#{@options.margin.left},#{@options.margin.top})")

    drawXAxis: (xAxis) ->
      @svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0,#{@options.height})")
        .call(xAxis)

    drawYAxis: (yAxis) ->
      @svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)

    drawAxes: ->
      @drawXAxis @xAxis
      @drawYAxis @yAxis

  init: (options) ->
    new Chart options
