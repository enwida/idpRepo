define ["generic_chart"], (generic_chart) ->

  class BarChart

    constructor: (options) ->
      # Deep clone the options object
      options = $.extend true, {}, options

      @makeDateScale options.lines if options.scale.x.type is "date"
      options.scale.x.type = "ordinal"
      @chart = generic_chart.init options

    drawBars: (data, id=0) ->
      console.log id
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

    makeDateScale: (data) ->
      lastformat = d3.time.format "%m"
      for line in data
        for dp, i in line.dataPoints
          next = line.dataPoints[i+1]
          if next?
            diff = Math.abs(next.x - dp.x)
            if diff <= 1000*60*60*24*28
              lastformat = d3.time.format "%d"
            else if diff <= 1000*60*60*24*356*3
              lastformat = d3.time.format "%m"
            else
              lastformat = d3.time.format "%Y"

          dp.x = lastformat new Date dp.x

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
