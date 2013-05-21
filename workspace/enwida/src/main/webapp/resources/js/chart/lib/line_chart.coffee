define ["generic_chart"], (generic_chart) ->

  class LineChart

    constructor: (options) ->
      @chart = generic_chart.init options

    generateLine: ->
      d3.svg.line()
        .x((d) => @chart.xScale(d.x))
        .y((d) => @chart.yScale(d.y))

    drawLine: (data, id=0) ->
      line = @generateLine()
      @chart.svg.append("path")
        .datum(data)
        .attr("class", "line line#{id}")
        .attr("data-legend", @chart.lineLabels[id])
        .attr("d", line)

    drawDots: (data, id=0) ->
      @chart.svg.selectAll("dot")
        .data(data)
          .enter().append("circle")
          .attr("class", "dot dot#{id}")
          .attr("r", 4.5)
          .attr("cx", (d) => @chart.xScale(d.x))
          .attr("cy", (d) => @chart.yScale(d.y))
          .attr("original-title", (d) =>
            x = d.x
            if @chart.options?.scale?.x?.type is "date"
              x = d3.time.format("%Y-%m-%d %H:%M") new Date x
            "#{@chart.xLabel}: #{x} #{@chart.yLabel}: #{d.y}"
          )

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      for lineData, i in @chart.data
        @drawLine lineData, i
        @drawDots lineData, i
      @chart.drawLegend()

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new LineChart options
