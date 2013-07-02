define ["generic_chart"], (generic_chart) ->

  class MinMaxChart

    constructor: (options) ->
      @chart = generic_chart.init options
      @chart.yScale.domain d3.extent @getAllYs()

      @barWidth = @calculateBarWidth()
      @barOffset = @barWidth / 2

      # Adjust the x scale range in order to leave space for the first
      # and the last bar
      @chart.xScale.range [@barOffset, @chart.options.width - @barOffset]

      # Recalculate bar width
      @barWidth = @calculateBarWidth()
      @barWidth *= 0.8 # Add padding
      @barOffset = @barWidth / 2

      console.log @chart.yScale.domain()
      console.log @barWidth

    calculateBarWidth: ->
      data = @chart.data[1]
      xLow = @chart.xScale(data[0].x)
      xHigh = @chart.xScale(data[0].x + @getSmallestDistance data, "x")
      xHigh - xLow

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
            ).html()
          )

    drawBars: (data, id=0) ->
      @chart.svg.selectAll(".foo")
        .data(data)
        .enter().append("rect")
          .attr("class", "bar bar#{id} visual#{id} line#{id} line")
          .attr("x", (d) => @chart.xScale(d.x) - @barOffset)
          .attr("y", (d) => @chart.yScale(d.max))
          .attr("width", @barWidth)
          .attr("height", (d) => @chart.yScale(d.min) - @chart.yScale(d.max))
          .attr("original-title", (d) =>
            x = d.x
            if @chart.options?.scale?.x?.type is "date"
              x = d3.time.format("%Y-%m-%d %H:%M") new Date x

            $("<div>")
              .append($("<h6>")
                .addClass("minmax")
                .addClass("tooltip#{id}")
                .text(@chart.lines[id].title))
              .append($("<table cellpadding='2'>")
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.xLabel)
                  .append($("<td align='left'>").append($("<b>").text x)))
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.yLabel)
                  .append($("<td align='left'>").append($("<b>").text "#{d.min} - #{d.max}")))
            ).html())

    getAllYs: ->
      allValues = []
      unless _(@chart.options.disabledLines).contains 0
        allValues = allValues.concat @chart.data[0].map (dp) -> dp.y
      unless _(@chart.options.disabledLines).contains 1
        allValues = allValues.concat @chart.data[1].map (dp) -> dp.min
        allValues = allValues.concat @chart.data[1].map (dp) -> dp.max
      allValues

    getSmallestDistance: (data, key="x") ->
      result = Infinity
      for i in [0...data.length-2]
        diff = Math.abs(data[i+1][key] - data[i][key])
        result = diff if diff < result
      result

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()

      # Modify the x scale to fill the whole svg width
      @chart.svg.select(".x.axis path").attr "d", "M0,6V0H#{@chart.options.width}V6"

      unless _(@chart.options.disabledLines).contains 1
        @drawBars @chart.data[1], 1

      @drawLine @chart.data[0], 0
      @drawDots @chart.data[0], 0

      @chart.drawLegend()

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new MinMaxChart options
