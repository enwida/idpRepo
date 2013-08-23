define ->

  flight.component ->

    @createElements = ->
      @$node
        .append($("<select>")
          .addClass("timezoneSelect")
          .append($("<option>")
            .attr("value", "local")
            .text("Local timestamps"))
          .append($("<option>")
            .attr("value", "utc")
            .text("UTC timestamps")))

    @setupEvents = ->
      @select("timezoneSelect").change =>
        @trigger "timeZoneSelectionChanged", timeZone: @select("timezoneSelect").val()

    @defaultAttrs
      timezoneSelect: ".timezoneSelect"

    @after "initialize", ->
      @createElements()
      @setupEvents()

      # Trigger first selection
      @select("timezoneSelect").change()
