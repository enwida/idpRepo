define ["util/time_utils"], (TimeUtils) ->

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
        .append($("<select>")
          .addClass("timezoneInfoSelect")
          .append($("<option>")
            .attr("value", "with")
            .text(@attr.navigationData.localizations.infoKeys.withTimezone))
          .append($("<option>")
            .attr("value", "without")
            .text(@attr.navigationData.localizations.infoKeys.withoutTimezone)))
        .append($("<input>")
          .addClass("preview")
          .attr("type", "text")
          .attr("name", "preview")
          .attr("readonly", true))

    @setupEvents = ->
      timezoneStream = @select("timezoneSelect").asEventStream "change", (e) -> $(e.target).val()
      timezoneInfoStream = @select("timezoneInfoSelect").asEventStream "change", (e) -> $(e.target).val()

      changeStream = Bacon.combineWith ((timezone, timezoneInfo) ->
        timezone: timezone, timezoneInfo: timezoneInfo
      ), timezoneStream, timezoneInfoStream

      changeStream.onValue (selections) =>
        @showPreview selections
        @trigger "timezoneSelectionChanged", selections

    @showPreview = (selections) ->
      date = new Date()
      date = TimeUtils.asUTC date if selections.timezone is "utc"
      format = if selections.timezoneInfo is "with"
        d3.time.format "%Y-%m-%d %H:%M %Z"
      else
        d3.time.format "%Y-%m-%d %H:%M"

      preview = format date
      # Modify the time zone information manually
      preview = preview.replace /\+\d{4}/, "+0000" if selections.timezone is "utc"
      @select("preview").val preview

    @defaultAttrs
      timezoneSelect: ".timezoneSelect"
      timezoneInfoSelect: ".timezoneInfoSelect"
      preview: ".preview"

    @after "initialize", ->
      @on "refresh", (_, opts) =>
        @attr.navigationData = opts.data
        @createElements()
        @setupEvents()

        # Trigger first selection
        @select("timezoneSelect").change()
        @select("timezoneInfoSelect").change()
