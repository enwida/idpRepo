define ["scale"], (scale) ->

  class Chart

    constructor: (options) ->
      throw "No data given" unless options? and options.lines?

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
      @lines = options.lines
      @xLabel = @options.xLabel ? ""
      @yLabel = @options.yLabel ? ""
      @lineLabels = @lines.map (line) -> line.title

      # @data is an array of line data
      @data = @lines.map (line) -> line.dataPoints
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
      @svg = d3.selectAll(@options.parent).append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
          .attr("transform", "translate(#{@options.margin.left},#{@options.margin.top})")

    drawXAxis: (xAxis) ->
      @svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(0,#{@options.height})")
        .call(xAxis)
        .append("text")
          .attr("x", @options.width)
          .attr("y", -5)
          .attr("text-anchor", "end")
          .text("#{@xLabel}")

      @makeDateScale() if @options.scale.x.type is "date"

    drawYAxis: (yAxis) ->
      @svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
        .append("text")
          .attr("transform", "rotate(-90)")
          .attr("y", 12)
          .attr("text-anchor", "end")
          .text("#{@yLabel}")

    drawLegend: ->
      return # Disable in-chart legend
      @legend = @svg.append("g")
        .attr("class","legend")
        .attr("transform","translate(50,30)")
        .attr("data-style-padding", 10)
        .call(d3.legend)

    drawAxes: ->
      @drawXAxis @xAxis
      @drawYAxis @yAxis

    makeDateScale: ->
      formats = @options?.x?.dateFormats ? [
        ["%Y", "%Y-%m-%d"]
        ["%m", "%b"]
        ["%d", "%d"]
        ["%H:%M", "%H:%M"]
      ]
      tickFormat = scale.getTickFormater(formats)()
      for tick in $(@options.parent).find("g.tick text")
        date = new Date parseInt $(tick).text().replace(/,/g, "")
        $(tick).text tickFormat date
        $(tick).attr "original-title", d3.time.format("%Y-%m-%d %H:%M") date
        $(tick).tipsy()

  init: (options) ->
    new Chart options
