define ["generic_chart"], (generic_chart) ->

  class CarpetChart

    constructor: (options) ->
      @chart = generic_chart.init options

    drawCarpet: (data) ->
      xDomain = @chart.xScale.domain()
      yDomain = @chart.yScale.domain()
      rectWidth = @chart.options.width / xDomain.length
      rectHeight = @chart.options.height / yDomain.length

      color = d3.scale.linear()
        .range(["#00f", "#f00"])
        .domain(d3.extent data.map (dp) -> dp.v)
      @chart.svg.selectAll(".carpet")
        .data(data)
        .enter().append("rect")
        .attr("class", "carpet")
        .attr("x", (d) => @chart.xScale(d.x))
        .attr("y", (d) => @chart.yScale(d.y) - rectHeight)
        .attr("fill", (d) -> color(d.v))
        .attr("width", rectWidth)
        .attr("height", rectHeight)

    draw: ->
      @chart.drawSvg()
      @chart.drawAxes()
      @drawCarpet @chart.data[0]

    redraw: ->
      @chart.svg = null
      @draw()

  init: (options) ->
    new CarpetChart options
