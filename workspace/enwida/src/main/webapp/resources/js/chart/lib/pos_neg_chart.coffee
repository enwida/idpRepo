define ["generic_chart", "scale"], (generic_chart, scale) ->

  class PosNegChart

    constructor: (options) ->
      @chart = generic_chart.init options

      @barWidth = scale.getBarWidth @chart.data[1], @chart.xScale, "x"
      @barOffset = @barWidth / 2

      # Adjust the x scale range in order to leave space for the first
      # and the last bar
      @chart.xScale.range [@barOffset, @chart.options.width - @barOffset]

      # Recalculate bar width
      @barWidth = scale.getBarWidth @chart.data[1], @chart.xScale, "x"
      @barWidth *= 0.8 # Add padding
      @barOffset = @barWidth / 2

      console.log @barWidth

    drawBars: (data, id=0, pos=true) ->
      fy = (d) => if pos then @chart.yScale(d.y) else @chart.yScale(0)
      fheight = (d) =>
        if pos
          @chart.yScale(0) - @chart.yScale(d.y)
        else
          @chart.yScale(d.y) - @chart.yScale(0)

      @chart.svg.selectAll(".foo")
        .data(data)
        .enter().append("rect")
          .attr("class", "bar bar#{id} visual#{id} line#{id} line")
          .attr("x", (d) => @chart.xScale(d.x) - @barOffset)
          .attr("y", fy)
          .attr("width", @barWidth)
          .attr("height", fheight)
          .attr("original-title", (d) =>
            x = d.x
            if @chart.options?.scale?.x?.type is "date"
              x = d3.time.format("%Y-%m-%d %H:%M") new Date x

            $("<div>")
              .append($("<h6>").addClass("tooltip#{id}").text @chart.lines[id].title)
              .append($("<table cellpadding='2'>")
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.xLabel)
                  .append($("<td align='left'>").append($("<b>").text x)))
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.yLabel)
                  .append($("<td align='left'>").append($("<b>").text d.y)))
            ).html())

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