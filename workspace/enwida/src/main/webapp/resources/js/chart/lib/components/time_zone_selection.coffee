define ->

  flight.component ->

    @createElements = ->
      @$node
        .append($("<select>")
          .addClass("timezoneSelect")
          .append($("<option>")
            .attr("value", "local")
            .text(@attr.navigationData.localizations.infoKeys.localTimestamps))
          .append($("<option>")
            .attr("value", "utc")
            .text(@attr.navigationData.localizations.infoKeys.utcTimestamps)))

    @setupEvents = ->
      @select("timezoneSelect").change =>
        @trigger "timeZoneSelectionChanged", timeZone: @select("timezoneSelect").val()

    @defaultAttrs
      timezoneSelect: ".timezoneSelect"

    @after "initialize", ->
      @on "refresh", (_, opts) =>
        console.log opts.data
        @attr.navigationData = opts.data
        @createElements()
        @setupEvents()

        # Trigger first selection
        @select("timezoneSelect").change()
