define [ "components/navigation"
         "components/visual"
         "components/lines"
         "util/loading"
         "util/lines_preprocessor"
        ],

  (Navigation
   Visual
   Lines
   Loading
   LinesPreprocessor
  ) ->

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

      @reportDisabledLines = (disabledLines) ->
        $.ajax "disabledLines",
          type: "POST"
          data:
            chartId: @attr.id
            lines: disabledLines.join ","
          error: (xhr, status, err) =>
            @logError "Error while reporting disabled lines: #{err}"
          success: =>
            @logDebug "Sent disabled lines"

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
        @reportDisabledLines opts.disabledLines
        @triggerDraw @attr.data

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"
        disabledLines: []

      @after "initialize", ->
        # Logging facilities
        @logDebug = Logger.logDebug "ChartManager"
        @logError = Logger.logError "ChartManager"

        # Event handlers
        @on "getLines", @onGetLines
        @on "updateNavigation", (_, opts) ->
          @attr.navigationData = opts.data
          @trigger @select("visual"), "navigationData", opts
          @trigger @select("lines"), "disabledLines",
            lines: opts.data?.defaults?.disabledLines
        @on "toggleLine", @toggleLine

        # Parse element attributes
        @attr.type = @$node.attr("data-chart-type") ? "line"
        @attr.width = parseInt(@$node.attr("data-width"))
        @attr.height = parseInt(@$node.attr("data-height"))

        @attr.width = 800 if isNaN @attr.width
        @attr.height = 800 if isNaN @attr.height

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual,
          id: @attr.id
          type: @attr.type
          width: @attr.width
          height: @attr.height

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

