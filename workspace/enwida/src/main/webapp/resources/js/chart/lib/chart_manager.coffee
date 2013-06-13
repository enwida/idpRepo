define ["navigation", "spreadsheet", "visual"],
  (Navigation, Spreadsheet, Visual) ->

    flight.component ->

      @getLines = (options, callback) ->
        format = d3.time.format "%Y-%m-%d"
        $.ajax "lines.test",
          data:
            chartId: @attr.id
            product: options.product
            tso: options.tso
            startTime: format new Date options.timeRange.from
            endTime: format new Date options.timeRange.to
            resolution: options.resolution
          success: (data) -> callback null, data
          error: (err) -> callback err

      @onGetLines = (a, opts) ->
        @getLines opts, (err, data) =>
          throw err if err?
          @trigger @select("visual"), "draw", data: data

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"

      @after "initialize", ->
        @on "getLines", @onGetLines
        @on "updateNavigation", (_, opts) ->
          @trigger @select("visual"), "navigationData", opts

        # Add navigation
        navigation = $("<div>").addClass "navigation"
        @$node.append navigation
        Navigation.attachTo navigation, id: @attr.id

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual,
          id: @attr.id
          type: @$node.attr("data-chart-type") ? "line"
