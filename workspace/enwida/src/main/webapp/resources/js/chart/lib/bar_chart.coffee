define ["generic_chart", "scale"], (generic_chart, scale) ->

  class BarChart

    constructor: (options) ->
      # Deep clone the options object
      options = $.extend true, {}, options

      # Change the x scale type temporarily when then scales
      # are initialized
      tmp = options.scale.x.type
      options.scale.x.type = "ordinal"
      @chart = generic_chart.init options
      options.scale.x.type = tmp

    drawBars: (data, id=0) ->
      barWidth = @chart.xScale.rangeBand() / data.length
      barOffset = 0

      @chart.svg.selectAll(".foo")
        .data(data[id])
        .enter().append("rect")
          .attr("class", "bar bar#{id} visual#{id} line#{id} line")
          .attr("x", (d) => @chart.xScale(d.x) - barOffset + id * barWidth)
          .attr("y", (d) => @chart.yScale(d.y))
          .attr("width", barWidth)
          .attr("height", (d) => @chart.options.height - @chart.yScale(d.y))
          .attr("original-title", (d) => @chart.getTooltip d, id)

    minInterval: (key, id=0) ->
      result = null
      lastValue = @chart.data[id][0][key]
      i = 1
      while i < @chart.data[id].length
        currentValue = @chart.data[id][i][key]
        distance = currentValue - lastValue
        result = distance if not result || result > distance
        lastValue = currentValue
        i += 1
      result

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      for line, i in @chart.data
        @drawBars @chart.data, i

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new BarChart options
