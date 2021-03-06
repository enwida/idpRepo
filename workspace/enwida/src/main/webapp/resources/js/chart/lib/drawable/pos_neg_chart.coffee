define ["./generic_chart", "util/scale"], (GenericChart, Scale) ->

  class PosNegChart

    constructor: (options) ->
      @chart = GenericChart.init options

      # Make sure we have 0 in the domain
      yDomain = @chart.yScale.domain()
      yDomain[0] = Math.min(0, yDomain[0])
      yDomain[1] = Math.max(0, yDomain[1])
      @chart.yScale.domain yDomain

      @barWidth = Scale.getBarWidth @chart.data[1], @chart.xScale, "x"
      @barOffset = @barWidth / 2

      # Adjust the x scale range in order to leave space for the first
      # and the last bar
      @chart.xScale.range [@barOffset, @chart.options.width - @barOffset]

      # Recalculate bar width
      @barWidth = Scale.getBarWidth @chart.data[1], @chart.xScale, "x"
      @barWidth *= 0.8 # Add padding
      @barOffset = @barWidth / 2

    drawBars: (data, id=0, pos=true) ->
      fy = (d) => if pos then @chart.yScale(d.y) else @chart.yScale(0)
      fheight = (d) =>
        if pos
          Math.max 0, @chart.yScale(0) - @chart.yScale(d.y)
        else
          Math.max 0, @chart.yScale(d.y) - @chart.yScale(0)

      @chart.svg.selectAll(".foo")
        .data(data)
        .enter().append("rect")
          .attr("class", "bar bar#{id} visual#{id} line#{id} line")
          .attr("x", (d) => @chart.xScale(d.x) - @barOffset)
          .attr("y", fy)
          .attr("width", @barWidth)
          .attr("height", fheight)
          .attr("original-title", (d) => @chart.getTooltip d, id)

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()

      # Modify the x scale to fill the whole svg width
      @chart.svg.select(".x.axis path").attr "d", "M0,6V0H#{@chart.options.width}V6"

      @drawBars @chart.data[0], 0, true
      @drawBars @chart.data[1], 1, false
      @chart.drawLegend()

  init: (options) ->
    new PosNegChart options
