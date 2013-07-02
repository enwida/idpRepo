define ["navigation", "spreadsheet", "visual", "lines", "loading", "lines_preprocessor"],
  (Navigation, Spreadsheet, Visual, Lines, Loading, LinesPreprocessor) ->

    flight.component ->

      @getMsg = ->
        Loading.of @select("visual"),
          @attr.navigationData?.width,
          @attr.navigationData?.height

      @getLines = (options, callback) ->
        @getMsg().showLoading()

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
          if data.length is 0
            return @getMsg().showText "No data"

          @attr.data = data = LinesPreprocessor.transform @attr.type, data
          @triggerDraw data
          @trigger @select("lines"), "updateLines", lines: data

      @triggerDraw = (data) ->
        if data.length is @attr.disabledLines.length
          return @getMsg().showText "No lines selected"

        @trigger @select("visual"), "draw",
          data: data
          disabledLines: @attr.disabledLines


      @toggleLine = (_, opts) ->
        @attr.disabledLines = opts.disabledLines
        @triggerDraw @attr.data

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"
        disabledLines: []

      @after "initialize", ->
        @on "getLines", @onGetLines
        @on "updateNavigation", (_, opts) ->
          @attr.navigationData = opts.data
          @trigger @select("visual"), "navigationData", opts
        @on "toggleLine", @toggleLine

        @attr.type = @$node.attr("data-chart-type") ? "line"
        @attr.width = parseInt(@$node.attr("data-width")) ? 800

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual,
          id: @attr.id
          type: @attr.type
          width: @attr.width

        # Add lines
        if @attr.type isnt "carpet"
          lines = $("<div>").addClass("lines").css("width", "#{@attr.width}px")
          @$node.append lines
          Lines.attachTo lines

        # Add navigation
        navigation = $("<div>").addClass "navigation"
        @$node.append navigation
        Navigation.attachTo navigation,
          id: @attr.id
          width: @attr.width
          type: @attr.type

