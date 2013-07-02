define ["./generic_chart"], (GenericChart) ->

  class LineChart

    constructor: (options) ->
      @chart = GenericChart.init options

    generateLine: ->
      d3.svg.line()
        .x((d) => @chart.xScale(d.x))
        .y((d) => @chart.yScale(d.y))

    drawLine: (data, id=0) ->
      line = @generateLine()
      @chart.svg.append("path")
        .datum(data)
        .attr("class", "line line#{id} visual#{id}")
        .attr("data-legend", @chart.lineLabels[id])
        .attr("d", line)

    drawDots: (data, id=0) ->
      @chart.svg.selectAll("dot")
        .data(data)
          .enter().append("circle")
          .attr("class", "dot dot#{id} visual#{id}")
          .attr("r", 4.5)
          .attr("cx", (d) => @chart.xScale(d.x))
          .attr("cy", (d) => @chart.yScale(d.y))
          .attr("original-title", (d) => @chart.getTooltip d, id)

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      for lineData, i in @chart.data
        @drawLine lineData, i
        @drawDots lineData, i
      @chart.drawLegend()

  init: (options) ->
    new LineChart options
