define ->

  flight.component ->

    @createElements = ->
      @$node
        .append($("<select>")
          .addClass("numberFormatSelect")
          .append($("<option>")
            .attr("value", "default")
            .text(@attr.navigationData.localizations.infoKeys.defaultNumberFormat))
          .append($("<option>")
            .attr("value", "de")
            .text(@attr.navigationData.localizations.infoKeys.germanNumberFormat)))

    @setupEvents = ->
      @select("numberFormatSelect").change =>
        @trigger "numberFormatSelectionChanged", numberFormat: @select("numberFormatSelect").val()

    @defaultAttrs
      numberFormatSelect: ".numberFormatSelect"

    @after "initialize", ->
      @on "refresh", (_, opts) =>
        @attr.navigationData = opts.data
        @createElements()
        @setupEvents()

        # Trigger first selection
        @select("numberFormatSelect").change()
