define [ "components/visual"
         "components/productSelection"
         "components/timeSelection"
         "components/lines"
         "components/infobox"
         "components/chart_download"
         "components/data_sheet"
         "util/loading"
         "util/lines_preprocessor"
         "util/resolution"
         "util/product_tree"
         "util/time_utils"
        ],

  (Visual
   ProductSelection
   TimeSelection
   Lines
   Infobox
   ChartDownload
   DataSheet
   Loading
   LinesPreprocessor
   Resolution
   ProductTree
   TimeUtils
  ) ->

    flight.component ->
      @applyVisibility = ->
        linesSelection   = @$node.find ".lines"
        productSelection = @$node.find ".productSelect"
        timeSelection    = @$node.find ".timeselect"

        if @attr.navigationData.hasLineSelection
          linesSelection.show()
        else
          linesSelection.hide()

        if @attr.navigationData.hasProductSelection
          productSelection.show()
        else
          productSelection.hide()

        if @attr.navigationData.hasTimeSelection
          timeSelection.show()
        else
          timeSelection.hide()

      @getMsg = ->
        Loading.of @select("visual"),
          @attr.navigationData?.width,
          @attr.navigationData?.height

      @getNavigationData = (callback) ->
        $.ajax "navigation",
          data: chartId: @attr.id
          error: (err) -> callback err
          success: (data) =>
            @logDebug "Got navigation data:"
            console.log data

            @navigationData = data
            callback null, data

      @getLines = (options, callback) ->
        @getMsg().showLoading()

        format = d3.time.format "%Y-%m-%d"
        $.ajax "lines",
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

      @onGetLines = (selections) ->
        # Check if time selection is within bounds
        leaf = @attr.treeHelper.traverse selections.tso, selections.product
        if not TimeUtils.isTimeRangeInside selections.timeRange, leaf.timeRange
          @logDebug "No line request made due to time restrictions"
          return

        # Calculate resolution
        selections.resolution = @optimalResolution selections

        @logDebug "Getting lines:"
        @logDebug JSON.stringify selections

        # Update info box
        selections.title = @attr.navigationData.title
        @trigger @select("infobox"), "updateInfo",
          navigationData: @attr.navigationData
          selections: selections

        @getLines selections, (err, data) =>
          if err?
            @logError err
            return @trigger "chartMessage", msg: "Sorry, something went wrong."
          if data.length is 0
            return @trigger "chartMessage", msg: "No data"

          @attr.data = data = LinesPreprocessor.transform @attr.type, data
          @triggerDraw data
          @trigger @select("lines"), "updateLines", lines: data
          @trigger @select("dataSheet"), "refresh",
            lines: data
            navigationData: @attr.navigationData

      @triggerDraw = (data) ->
        if data.length is @attr.disabledLines.length
          return @trigger "chartMessage", msg: "No lines selected"

        @trigger @select("visual"), "draw",
          data: data
          navigation: @attr.navigationData
          disabledLines: @attr.disabledLines

        @trigger @select("chartDownload"), "downloadLink"

      @toggleLine = (_, opts) ->
        @attr.disabledLines = opts.disabledLines
        @reportDisabledLines opts.disabledLines
        @triggerDraw @attr.data

      @optimalResolution = (selections) ->
        leaf = @attr.treeHelper.traverse selections.tso, selections.product
        Resolution.getOptimalResolution \
          @attr.type, selections.timeRange,
          leaf.resolution, @attr.width, @attr.navigationData.aspects.length

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"
        infobox: ".infobox"
        productSelection: ".productSelection"
        timeSelection: ".timeSelection"
        chartDownload: ".chartDownload"
        dataSheet: ".dataSheet"
        disabledLines: []

      @after "initialize", ->
        # Logging facilities
        @logDebug = Logger.logDebug "ChartManager"
        @logError = Logger.logError "ChartManager"

        # Event handlers
        @on "getLines", @onGetLines
        @on "toggleLine", @toggleLine
        @on "errorMessage", (_, opts) ->
          @$node.text opts.msg
        @on "chartMessage", (_, opts) ->
          @getMsg().showText opts.msg

        # Compare methods
        productSelectionEquals = -> false # TODO
        timeSelectionEquals = (a, b) ->
          new Date(a.timeRange.from).getTime() == new Date(b.timeRange.from).getTime() and \
          new Date(a.timeRange.to).getTime() == new Date(b.timeRange.to).getTime()

        # Streams from selection subsystems
        productStream = @$node.asEventStream("productSelectionChanged", (_, v) -> v)
        timeStream = @$node.asEventStream("timeSelectionChanged", (_, v) -> v)
        selectionStream = productStream.toProperty().sampledBy timeStream, $.extend

        # Order is important here!
        # Call onValue on selectionStream before calling it on productStream
        selectionStream.onValue (selections) =>
          @onGetLines selections

        productStream.onValue (selections) =>
          leaf = @attr.treeHelper.traverse selections.tso, selections.product
          @trigger @select("timeSelection"), "timeRestrictions", leaf.timeRange
          @trigger @select("timeSelection"), "requestTimeSelection"

        # Parse element attributes
        @attr.type = @$node.attr("data-chart-type") ? "line"
        @attr.width = parseInt(@$node.attr("data-width"))
        @attr.height = parseInt(@$node.attr("data-height"))

        @attr.width = 800 if isNaN @attr.width
        @attr.height = 800 if isNaN @attr.height

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual, @attr

        # Add controls
        controls = $("<div>")
          .addClass("controls")
          .css("width", "#{@attr.width}px")
        @$node.append controls

        # Add info box
        infoBox = $("<div>").addClass("infobox")
        controls.append infoBox
        Infobox.attachTo infoBox

        # Add lines
        if @attr.type isnt "carpet"
          lines = $("<div>").addClass("lines")
          controls.append lines
          Lines.attachTo lines

        selection = $("<div>").addClass("selection")
        controls.append selection

        # Add product selection
        productSelection = $("<div>").addClass "productSelection"
        selection.append productSelection
        ProductSelection.attachTo productSelection, @attr

        # Add time selection
        timeSelection = $("<div>").addClass "timeSelection"
        selection.append timeSelection
        TimeSelection.attachTo timeSelection, @attr


        # Add buttons
        buttons = $("<div>").addClass "buttons"
        controls.append buttons

        # SVG download
        chartDownload = $("<div>").addClass("chartDownload")
        buttons.append chartDownload
        ChartDownload.attachTo chartDownload, @attr

        # CSV download
        buttons.append($("<a>")
          .addClass("downloadCsv")
          .attr("href", "download?chartId=#{@attr.id}")
          .append("<button>")
            .addClass("btn")
            .text("Download CSV"))

        # Data sheet toggle
        buttons.append($("<button>")
          .addClass("btn")
          .text("Data sheet")
          .click => @select("dataSheet").toggle())

        # Add data sheet
        dataSheet = $("<div>").addClass("dataSheet").hide()
        controls.append dataSheet
        DataSheet.attachTo dataSheet, @attr

        @getNavigationData (err, data) =>
          if err?
            @logError err
            return @trigger "errorMessage", msg: "Sorry, something went wrong."
          unless typeof data is "object" and data?.allResolutions?.length > 0
            return @trigger "errorMessage", msg: "Sorry, you do not have the permission to see this chart."
            return

          @attr.navigationData = data
          @attr.treeHelper = ProductTree.init data
          @applyVisibility()

          @trigger @select("lines"), "disabledLines", lines: _(data.defaults.disabledLines).values()
          @trigger @select("timeSelection"), "refresh", data: data
          @trigger @select("productSelection"), "refresh", data: data

