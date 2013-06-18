define ["navigation", "spreadsheet", "visual", "lines"],
  (Navigation, Spreadsheet, Visual, Lines) ->

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
          @trigger @select("lines"), "updateLines", lines: data

      @toggleLine = (_, opts) ->
        @$node.find("path.line#{opts.lineId}").toggle()
        @$node.find(".dot#{opts.lineId}").toggle()

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"

      @after "initialize", ->
        @on "getLines", @onGetLines
        @on "updateNavigation", (_, opts) ->
          @trigger @select("visual"), "navigationData", opts
        @on "toggleLine", @toggleLine

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual,
          id: @attr.id
          type: @$node.attr("data-chart-type") ? "line"

        # Add lines
        lines = $("<div>").addClass("lines")
          .css("width", @$node.attr("data-width") ? "100%")
        @$node.append lines
        Lines.attachTo lines

        # Add navigation
        navigation = $("<div>").addClass "navigation"
        @$node.append navigation
        Navigation.attachTo navigation, id: @attr.id

