define ["./generic_chart", "util/scale"], (GenericChart, Scale) ->

  class CarpetChart

    constructor: (options) ->
      @chart = GenericChart.init options
      @chart.options.scale.x.type = "date"

      # Setup y scale
      yDomain = _(@chart.data[0].map (dp) -> dp.y).uniq().sort (a,b) -> a-b
      yDomain.push yDomain[yDomain.length - 1] + 1
      @chart.yScale.domain [yDomain[0], yDomain[yDomain.length - 1]]

      # Calculate bar width/height
      @rectWidth = Scale.getBarWidth(@chart.data[0], @chart.xScale, "x") + 1
      @rectHeight = (@chart.options.height / (yDomain.length - 1)) + 1

    drawCarpet: (data, id=0) ->
      extent = d3.extent data.map (dp) -> dp.v
      limit  = Math.max Math.abs(extent[0]), Math.abs(extent[1])
      color1 = @chart.options.colors?[0] ? "#0000FF"
      color2 = @chart.options.colors?[1] ? "#FFFFFF"
      color3 = @chart.options.colors?[2] ? "#FF0000"

      @colorPos = d3.scale.linear()
        .range([color2, color1])
        .domain([0, limit])

      @colorNeg = d3.scale.linear()
        .range([color3, color2])
        .domain([-1 * limit, 0])

      @chart.svg.selectAll(".carpet")
        .data(data)
        .enter().append("rect")
        .attr("class", "carpet")
        .attr("x", (d) => @chart.xScale(d.x))
        .attr("y", (d) => @chart.yScale(d.y) - @rectHeight)
        .attr("fill", (d) =>
          color = if d.v < 0 then @colorNeg else @colorPos
          color d.v
        )
        .attr("width", @rectWidth)
        .attr("height", @rectHeight)
        .attr("original-title", (d) =>
            date = new Date d.x
            date.setHours d.y
            x = d3.time.format("%Y-%m-%d %H:%M") date

            @chart.getTooltipHtml id,
              @chart.lines[id].title
              @chart.xLabel
              @chart.yLabel
              x
              d.v
          )

    drawLegend: ->
      margin = @chart.options.margin
      svg = d3.selectAll(@chart.options.parent).append("svg")
        .attr("class", "legend")
        .attr("width", 150)
        .attr("height", @chart.options.height + margin.bottom + margin.top + 30)
        .append("g")
          .attr("transform", "translate(0,#{margin.top + 30})")

      baseNeg = @colorNeg.domain()[0]
      stepNeg = (@colorNeg.domain()[1] - baseNeg) / 5
      basePos = @colorPos.domain()[0]
      stepPos = (@colorPos.domain()[1] - basePos) / 5
      barHeight = @chart.options.height / 10

      for i in [0...5]
        svg.append("rect")
          .attr("x", 0)
          .attr("y", i * barHeight)
          .attr("width", 20)
          .attr("height", barHeight)
          .attr("fill", @colorPos basePos + (5-i) * stepPos)

      for i in [0...5]
        svg.append("rect")
          .attr("x", 0)
          .attr("y", (i + 5) * barHeight)
          .attr("width", 20)
          .attr("height", barHeight)
          .attr("fill", @colorNeg baseNeg + (4-i) * stepNeg)

      for i in [0..5]
        y = i * barHeight
        y += 1 if i is 0
        y -= 1 if i is 5

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
          .text(Math.round((basePos + (5-i) * stepPos) * 1000) / 1000)

      for i in [1..5]
        y = (i + 5) * barHeight
        y += 1 if i is 0
        y -= 1 if i is 5

        svg.append("line")
          .attr("x1", 20)
          .attr("x2", 26)
          .attr("y1", y)
          .attr("y2", y)
          .attr("fill", "#000000")
          .attr("stroke", "#000000")
        svg.append("text")
          .attr("x", 30)
          .attr("y", (i + 5) * barHeight + 3)
          .text(Math.round((baseNeg + (5-i) * stepNeg) * 1000) / 1000)

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
