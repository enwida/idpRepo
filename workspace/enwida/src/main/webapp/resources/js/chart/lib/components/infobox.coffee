define ->

  flight.component ->

    @createElements = ->
      @$node.append $("<table>")

    @update = (opts) ->
      localizations = opts.navigationData.localizations
      infoKeys = localizations.infoKeys
      selections = opts.selections
      formatDate = d3.time.format "%Y-%m-%d"

      title = localizations.title
      timeRange = formatDate(opts.selections.timeRange.from) + " - " \
                + formatDate(opts.selections.timeRange.to)
      resolution = localizations.resolutions[selections.resolution]
      tso = localizations.tsos[selections.tso]

      # Construct product from selection
      product = ""
      @$node.closest(".chart").find(".productSelect select")[1..3].each ->
        product += $(@).find("option:selected").text() + " "

      @select("table").empty()
      @select("table").attr("cellpadding", 3)
        .append($("<tr>")
          .append($("<td>").text(infoKeys.title))
          .append($("<td>").append($("<b>").text(opts.navigationData.title))))
        .append($("<tr>")
          .append($("<td>").text(infoKeys.product))
          .append($("<td>").append($("<b>").text(product))))
        .append($("<tr>")
          .append($("<td>").text(infoKeys.timerange))
          .append($("<td>").append($("<b>").text(timeRange))))
        .append($("<tr>")
          .append($("<td>").text(infoKeys.resolution))
          .append($("<td>").append($("<b>").text(resolution))))
        .append($("<tr>")
          .append($("<td>").text(infoKeys.tso))
          .append($("<td>").append($("<b>").text(tso))))

    @defaultAttrs
      table: "table"

    @after "initialize", ->
      @createElements()
      @on "updateInfo", (_, selections) => @update selections
