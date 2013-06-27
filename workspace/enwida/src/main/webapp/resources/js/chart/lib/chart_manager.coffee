define ["navigation", "spreadsheet", "visual", "lines", "loading"],
  (Navigation, Spreadsheet, Visual, Lines, Loading) ->

    flight.component ->
      @getLines = (options, callback) ->
        Loading.showLoading @select("visual"),
          @attr.navigationData?.width,
          @attr.navigationData?.height

        format = d3.time.format "%Y-%m-%d"
        $.ajax "lines.test",
          data:
            chartId: @attr.id
            product: options.product
            tso: options.tso
            startTime: format options.timeRange.from
            endTime: format options.timeRange.to
            resolution: options.resolution
          success: (data) =>
            callback null, data
          error: (err) =>
            callback err

      @onGetLines = (a, opts) ->
        @getLines opts, (err, data) =>
          throw err if err?
          @trigger @select("visual"), "draw", data: data
          @trigger @select("lines"), "updateLines", lines: data

      @toggleLine = (_, opts) ->
        duration = opts.duration ? 200
        @$node.find(".visual .line#{opts.lineId}").toggle(duration)
        @$node.find(".dot#{opts.lineId}").toggle(duration)

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"

      @after "initialize", ->
        @on "getLines", @onGetLines
        @on "updateNavigation", (_, opts) ->
          @attr.navigationData = opts.data
          @trigger @select("visual"), "navigationData", opts
        @on "toggleLine", @toggleLine

        @attr.width = parseInt(@$node.attr("data-width")) ? 800

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual,
          id: @attr.id
          type: @$node.attr("data-chart-type") ? "line"
          width: @attr.width

        # Add lines
        lines = $("<div>").addClass("lines").css("width", "#{@attr.width}px")
        @$node.append lines
        Lines.attachTo lines

        # Add navigation
        navigation = $("<div>").addClass "navigation"
        @$node.append navigation
        Navigation.attachTo navigation,
          id: @attr.id
          width: @attr.width
          type: @$node.attr("data-chart-type") ? "line"

