define ->

  flight.component ->

    @createElements = ->
      @$node.append $("<table>")

    @update = (opts) ->
      formatDate = d3.time.format "%Y-%m-%d"
      title = opts.navigationData.title
      product = ""
      @$node.closest(".chart").find(".productSelect select")[1..3].each ->
        product += $(@).find("option:selected").text() + " "
      timeRange = formatDate(opts.selections.timeRange.from) + " - " \
                + formatDate(opts.selections.timeRange.to)
      resolution = opts.selections.resolution
      tso = opts.navigationData.tsos[@$node.closest(".chart").find(".tso").val()]

      @select("table").empty()
      @select("table").attr("cellpadding", 3)
        .append($("<tr>")
          .append($("<td>").text("Title"))
          .append($("<td>").append($("<b>").text(opts.navigationData.title))))
        .append($("<tr>")
          .append($("<td>").text("Product"))
          .append($("<td>").append($("<b>").text(product))))
        .append($("<tr>")
          .append($("<td>").text("Time range"))
          .append($("<td>").append($("<b>").text(timeRange))))
        .append($("<tr>")
          .append($("<td>").text("Resolution"))
          .append($("<td>").append($("<b>").text(resolution))))
        .append($("<tr>")
          .append($("<td>").text("TSO"))
          .append($("<td>").append($("<b>").text(tso))))

    @defaultAttrs
      table: "table"

    @after "initialize", ->
      @createElements()
      @on "updateInfo", (_, selections) => @update selections
