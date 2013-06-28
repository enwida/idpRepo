define ["generic_chart"], (generic_chart) ->

  class CarpetChart

    constructor: (options) ->
      @chart = generic_chart.init options
      @chart.options.scale.x.type = "date"

      # Setup y scale
      yDomain = _(@chart.data[0].map (dp) -> dp.y).uniq().sort (a,b) -> a-b
      console.log yDomain
      yDomain.push yDomain[yDomain.length - 1] + 1
      @chart.yScale.domain [yDomain[0], yDomain[yDomain.length - 1]]

      # Calculate bar width/height
      xDomain = @chart.xScale.domain()
      @rectWidth = @chart.options.width / xDomain.length
      @rectHeight = @chart.options.height / (yDomain.length - 1)

    drawCarpet: (data, id=0) ->
      @color = d3.scale.linear()
        .range(["#00f", "#f00"])
        .domain(d3.extent data.map (dp) -> dp.v)
      @chart.svg.selectAll(".carpet")
        .data(data)
        .enter().append("rect")
        .attr("class", "carpet")
        .attr("x", (d) => @chart.xScale(d.x))
        .attr("y", (d) => @chart.yScale(d.y) - @rectHeight)
        .attr("fill", (d) => @color(d.v))
        .attr("width", @rectWidth)
        .attr("height", @rectHeight)
        .attr("original-title", (d) =>
            date = new Date d.x
            date.setHours d.y
            x = d3.time.format("%Y-%m-%d %H:%M") date

            $("<div>")
              .append($("<h6>").addClass("tooltip#{id}").text @chart.lines[id].title)
              .append($("<table cellpadding='2'>")
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.xLabel)
                  .append($("<td align='left'>").append($("<b>").text x)))
                .append($("<tr>")
                  .append($("<td align='left'>").text @chart.yLabel)
                  .append($("<td align='left'>").append($("<b>").text d.v)))
            ).html()
          )

    drawLegend: ->
      margin = @chart.options.margin
      svg = d3.selectAll(@chart.options.parent).append("svg")
        .attr("width", 150)
        .attr("height", @chart.options.height + margin.bottom + margin.top + 30)
        .append("g")
          .attr("transform", "translate(0,#{margin.top + 30})")

      base = @color.domain()[0]
      step = (@color.domain()[1] - base) / 10
      barHeight = @chart.options.height / 10

      for i in [0...10]
        svg.append("rect")
          .attr("x", 0)
          .attr("y", i * barHeight)
          .attr("width", 20)
          .attr("height", barHeight)
          .attr("fill", @color base + (10-i+1) * step)

      for i in [0..10]
        y = i * barHeight
        y += 1 if i is 0
        y -= 1 if i is 10

        svg.append("line")
          .attr("x1", 20)
          .attr("x2", 26)
          .attr("y1", y)
          .attr("y2", y)
          .attr("fill", "#000000")
          .attr("stroke", "#000000")
        svg.append("text")
          .attr("x", 30)
          .attr("y", i * barHeight + 3)
          .text(Math.round((base + (10-i) * step) * 1000) / 1000)

      svg.append("text")
        .attr("x", 0)
        .attr("y", -25)
        .text(@chart.options.yLabel)
        .attr("font-weight", "bold")

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      @drawCarpet @chart.data[0]
      @drawLegend()

  init: (options) ->
    new CarpetChart options
