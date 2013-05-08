define ["generic_chart"], (generic_chart) ->

  class LineChart

    constructor: (options) ->
      @chart = generic_chart.init options

    generateLine: ->
      d3.svg.line()
        .x((d) => @chart.xScale(d.x))
        .y((d) => @chart.yScale(d.y))

    drawLine: (data) ->
      line = @generateLine()
      @chart.svg.append("path")
        .datum(data)
        .attr("class", "line")
        .attr("d", line)

    drawDots: (data) ->
      @chart.svg.selectAll("dot")
        .data(data)
          .enter().append("circle")
          .attr("class", "dot")
          .attr("r", 4.5)
          .attr("cx", (d) => @chart.xScale(d.x))
          .attr("cy", (d) => @chart.yScale(d.y))
          .attr("fill", "rgb(40, 100, 150)")

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      for lineData in @chart.data
        @drawLine lineData
        @drawDots lineData

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new LineChart options