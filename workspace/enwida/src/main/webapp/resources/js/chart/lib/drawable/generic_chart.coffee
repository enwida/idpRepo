define ["util/scale"], (Scale) ->

  class Chart

    constructor: (options) ->
      throw "No data given" unless options? and options.lines?

      default_options =
        margin:
          top: 20
          left: 80
          right: 20
          bottom: 60
        width: 960
        height: 500
        parent: "body"
        disabledLines: []

      @options = $.extend default_options, options
      @lines = options.lines
      @xLabel = @options.xLabel ? ""
      @yLabel = @options.yLabel ? ""
      @lineLabels = @lines.map (line) -> line.title

      # @data is an array of line data
      @data = @lines.map (line) -> line.dataPoints
      Scale.init @
      @generateAxes()

    generateXAxis: ->
      @xAxis = d3.svg.axis().scale(@xScale).orient("bottom")

    generateYAxis: ->
      @yAxis = d3.svg.axis().scale(@yScale).orient("left")

    generateAxes: (xScale, yScale) ->
      @generateXAxis()
      @generateYAxis()

    drawSvg: ->
      return if @svg?
      width = @options.width + @options.margin.left + @options.margin.right
      height = @options.height + @options.margin.top + @options.margin.bottom
      @svg = d3.selectAll(@options.parent).append("svg")
        .attr("width", width)
        .attr("height", height)
      @transferDataAttributes @options.parent.parent(), @svg
      @svg = @svg.append("g")
        .attr("transform", "translate(#{@options.margin.left},#{@options.margin.top})")

    drawXAxis: (xAxis, dx=0, dy=0) ->
      @svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(#{dx},#{dy + @options.height})")
        .call(xAxis)
        .append("text")
          .attr("x", @options.width / 2)
          .attr("y", 50)
          .attr("text-anchor", "middle")
          .attr("font-style", "italic")
          .text("#{@xLabel}")

      @makeDateScale() if @options.scale.x.type is "date"
      @avoidOverlaps()

    drawYAxis: (yAxis) ->
      @svg.append("g")
        .attr("class", "y axis")
        .call(yAxis)
        .append("text")
          .attr("transform", "rotate(-90),translate(-#{@options.height / 2},-60)")
          .attr("x", 0)
          .attr("y", 0)
          .attr("text-anchor", "middle")
          .attr("font-style", "italic")
          .text("#{@yLabel}")

    drawLegend: ->
      return # Disable in-chart legend
      @legend = @svg.append("g")
        .attr("class","legend")
        .attr("transform","translate(50,30)")
        .attr("data-style-padding", 10)
        .call(d3.legend)

    drawAxes: (dx=0, dy=0) ->
      @drawXAxis @xAxis, dx, dy
      @drawYAxis @yAxis

    makeDateScale: ->
      formats = @options?.scale?.x?.dateFormats ? [
        ["%Y", "%Y-%m-%d"]
        ["%m", "%b"]
        ["%d", "%d"]
        ["%H:%M", "%H:%M"]
      ]
      tickFormat = Scale.getTickFormater(formats)()
      for tick in $(@options.parent).find("g.tick text")
        $tick = $(tick)
        date = new Date parseInt $(tick).text().replace(/,/g, "")
        tickText = tickFormat date
        $tick.text tickText
        $tick.attr "original-title", d3.time.format("%Y-%m-%d %H:%M") date
        $tick.tipsy()

    avoidOverlaps: ->
      lastOffset = 0
      lastWidth = 0
      dodgeDown = true
      for tick in $(@options.parent).find("g.tick text")
        $tick = $(tick)
        xOffset = parseInt $tick.closest("g").attr("transform").match(/translate\(([0-9.]+),.+/)[1]
        width = $tick.width()
        if xOffset - lastOffset < lastWidth
          if dodgeDown
            $tick.attr "dy", "2em"
            dodgeDown = false
        else
          dodgeDown = true

        lastOffset = xOffset
        lastWidth = width

    transferDataAttributes: (source, target) ->
      attributes = d3.selectAll(source)[0][0].attributes
      for attribute in attributes
        if attribute.name.match /^data-/
          target.attr attribute.name, attribute.value

    getTooltip: (dp, id, fy) ->
      x = dp.x
      if @options?.scale?.x?.type is "date"
        x = d3.time.format("%Y-%m-%d %H:%M") new Date x

      y = dp.y
      if typeof fy is "function"
        y = fy dp

      @getTooltipHtml id, @lines[id].title, @xLabel, @yLabel, x, y

    getTooltipHtml: (id, title, xLabel, yLabel, x, y) ->
      $("<div>")
        .append($("<h6>").addClass("tooltip#{id}").text title)
        .append($("<table cellpadding='2'>")
          .append($("<tr>")
            .append($("<td align='left'>").text xLabel)
            .append($("<td align='left'>").append($("<b>").text x)))
          .append($("<tr>")
            .append($("<td align='left'>").text yLabel)
            .append($("<td align='left'>").append($("<b>").text y)))
      ).html()

  init: (options) ->
    new Chart options
