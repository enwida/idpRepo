define ["generic_chart"], (generic_chart) ->

  class BarChart

    constructor: (options) ->
      @chart = generic_chart.init options

    drawBars: (data) ->
      scaleOptions = @chart.options.scale
      isOrdinal = scaleOptions && scaleOptions.x && scaleOptions.x.type is "ordinal"
      if isOrdinal
        barWidth = @chart.xScale.rangeBand()
        barOffset = 0
      else
        barWidth = @chart.xScale(@minInterval("x")) - @chart.xScale(0)
        barWidth *= 0.9
        barOffset = barWidth / 2

      @chart.svg.selectAll(".bar")
        .data(data)
        .enter().append("rect")
          .attr("class", "bar")
          .attr("x", (d) => @chart.xScale(d.x) - barOffset)
          .attr("y", (d) => @chart.yScale(d.y))
          .attr("width", barWidth)
          .attr("height", (d) => @chart.options.height - @chart.yScale(d.y))
          .attr("fill", (d) -> "#0000aa")

    minInterval: (key) ->
      result = null
      lastValue = @chart.data[0][key]
      i = 1
      while i < @chart.data.length
        currentValue = @chart.data[i][key]
        distance = currentValue - lastValue
        result = distance if not result || result > distance
        lastValue = currentValue
        i += 1
      result

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      @drawBars @chart.data[0]

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new BarChart options
