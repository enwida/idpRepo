define ->

  showLoading: (element, width=960, height=600) ->
    element = d3.selectAll(element)
    svg = element.select("svg")

    if svg.empty()
      svg = element.append("svg")
        .attr("width", width)
        .attr("height", height)
    else
      width = parseInt svg.attr "width"
      height = parseInt svg.attr "height"

    svg.selectAll("*").remove()
    svg
      .append("g").attr("transform", "translate(#{width/2},#{height/2})")
        .append("text")
          .attr("font-size", "24")
          .style("text-anchor", "middle")
          .text("Loading...")
