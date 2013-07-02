define ["navigation", "spreadsheet", "visual", "lines", "loading"],
  (Navigation, Spreadsheet, Visual, Lines, Loading) ->

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

          @attr.data = data = @preprocessLines data
          @triggerDraw data
          @trigger @select("lines"), "updateLines", lines: data

      @triggerDraw = (data) ->
        if data.length is @attr.disabledLines.length
          return @getMsg().showText "No lines selected"

        @trigger @select("visual"), "draw",
          data: data
          disabledLines: @attr.disabledLines

      @preprocessLines = (lines) ->
        switch @attr.type
          when "minmax"
            result = []
            # Add avg line
            result.push lines[1]

            # Generate min/max line
            minMaxLine = lines[0]
            minMaxLine.title += " / #{lines[2].title}"
            for dp, i in minMaxLine.dataPoints
              dp.min = dp.y
              dp.max = lines[2].dataPoints[i].y

            result.push minMaxLine
            lines = result
          when "carpet"
            # Use only first line
            line = lines[0]
            hourFormat = d3.time.format "%H"
            for dp in line.dataPoints
              date = new Date dp.x
              dp.v = dp.y
              dp.y = parseInt hourFormat date
              date.setHours 0
              date.setMinutes 0
              date.setSeconds 0
              date.setMilliseconds 0
              dp.x = date.getTime()
            lines = [line]
          when "posneg"
            # Calculate negative line
            for dp in lines[1].dataPoints
              dp.y *= -1
            # Strip remaining lines
            lines = [lines[0], lines[1]]

        lines

      @toggleLine = (_, opts) ->
        @attr.disabledLines = opts.disabledLines
        duration = opts.duration ? 200
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

