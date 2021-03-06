define [ "components/visual"
         "components/product_selection"
         "components/extended_time_selection"
         "components/lines"
         "components/infobox"
         "components/chart_download"
         "components/time_zone_selection"
         "components/number_format_selection"
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
   TimeZoneSelection
   NumberFormatSelection
   Loading
   LinesPreprocessor
   Resolution
   ProductTree
   TimeUtils
  ) ->

    flight.component ->
      @dateFormat = d3.time.format "%Y-%m-%d"

      @setVisible = (element, visible) ->
        if visible
          element.show()
        else
          element.hide()

      @applyVisibility = ->
        linesSelection   = @$node.find ".lines"
        productSelection = @$node.find ".productSelect"
        timeSelection    = @$node.find ".timeselect"

        @setVisible linesSelection, @attr.navigationData.options.hasLineSelection
        @setVisible productSelection, @attr.navigationData.options.hasProductSelection
        @setVisible timeSelection, @attr.navigationData.options.hasTimeSelection

      @getMsg = ->
        Loading.of @select("visual"),
          @attr.navigationData?.width,
          @attr.navigationData?.height

      @getNavigationData = (callback) ->
        $.ajax "navigation",
          data: chartId: @attr.id
          error: (err) -> callback err
          success: (data) =>
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

      @onGetLines = (selections) ->
        # Check for duplicate (resolution and time zone does not matter for preview chart)
        return if @attr.lastSelections? and
          @attr.lastSelections.tso is selections.tso and
          @attr.lastSelections.product is selections.product and
          @attr.lastSelections.timeRange.from.getTime() is selections.timeRange.from.getTime() and
          @attr.lastSelections.timeRange.to.getTime() is selections.timeRange.to.getTime()

        @attr.lastSelections = $.extend {}, selections

        # Calculate resolution
        selections.resolution = @optimalResolution selections

        # Update info box
        selections.title = @attr.navigationData.title
        @trigger @select("infobox"), "updateInfo",
          navigationData: @attr.navigationData
          selections: selections

        @getLines selections, (err, data) =>
          if err?
            @logError err
            return @trigger "chartMessage", msg: "Sorry, something went wrong."
          if data.length is 0 or _.max(data.map (line) -> line.dataPoints.length) < 2
            return @trigger "chartMessage", msg: "No data"

          @attr.data = data
          @triggerDraw data
          @trigger @select("lines"), "updateLines", lines: data

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
        @setDownloadLink()
        @triggerDraw @attr.data

      @optimalResolution = (selections) ->
        leaf = @attr.treeHelper.traverse selections.tso, selections.product
        Resolution.getOptimalResolution \
          @attr.type, selections.timeRange,
          leaf.resolution, @attr.width, @attr.navigationData.aspects.length

      @setDownloadLink = ->
        if @attr.disabledLines.length is @attr.navigationData.aspects.length
          @select("downloadLink").attr "href", "#"
          return

        selections = $.extend {},
          @attr.selections,
          resolution: @attr.downloadResolution
          disabledLines: @attr.disabledLines

        query =
          chartId: @attr.id
          product: selections.product
          tso: selections.tso
          startTime: @dateFormat selections.timeRange.from
          endTime: @dateFormat selections.timeRange.to
          resolution: selections.resolution
          timezone: selections.timezone
          timezoneInformation: selections.timezoneInfo
          numberFormat: selections.numberFormat
          disabledLines: selections.disabledLines.join ","

        urlQuery = (_(_(query).keys()).map (key) ->
          [key, query[key]].join "="
        ).join "&"
        @select("downloadLink").attr "href", "csv?" + urlQuery

      @defaultAttrs
        navigation: ".navigation"
        visual: ".visual"
        lines: ".lines"
        infobox: ".infobox"
        productSelection: ".productSelection"
        timeSelection: ".timeSelection"
        timezoneSelection: ".timezoneSelection"
        numberFormatSelection: ".numberFormatSelection"
        download: ".download"
        downloadLink: ".downloadLink"
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

        # Setup streams
        productStream = @$node.asEventStream("productSelectionChanged", (_, v) -> v)
        timeStream = @$node.asEventStream("timeSelectionChanged", (_, v) -> v)
        timezoneStream = @$node.asEventStream("timezoneSelectionChanged", (_, v) -> v)
        numberFormatStream = @$node.asEventStream("numberFormatSelectionChanged", (_, v) -> v)
        selectionStream = Bacon.combineWith $.extend, productStream, timeStream, timezoneStream, numberFormatStream

        # Request lines and update button test
        selectionStream.onValue (selections) =>
          @attr.selections = $.extend {}, selections
          @attr.downloadResolution = selections.resolution
          dataSets = TimeUtils.dataSetCount selections.timeRange,
            selections.resolution,
            @$node.find(".timeslot option").length

          text = @attr.navigationData.localizations.infoKeys.buttonText
          @select("download").text text.replace "$1", parseInt dataSets
          @setDownloadLink()
          @onGetLines selections

        # Forward time restrictions and request new time selections
        productStream.onValue (selections) =>
          leaf = @attr.treeHelper.traverse selections.tso, selections.product
          @trigger @select("timeSelection"), "timeRestrictions", leaf
          @trigger @select("timeSelection"), "requestTimeSelection", leaf

        # Parse element attributes
        @attr.type = @$node.attr("data-chart-type") ? "line"
        @attr.width = 600
        @attr.height = 300

        # Add visual
        visual = $("<div>").addClass "visual"
        @$node.append visual
        Visual.attachTo visual, @attr

        # Add info box
        #infoBox = $("<div>").addClass("infobox").css("width", "#{@attr.width}px")
        #@$node.append infoBox
        #Infobox.attachTo infoBox

        # Add lines
        if @attr.type isnt "carpet"
          lines = $("<div>").addClass("lines").css("width", "#{@attr.width}px")
          @$node.append lines
          Lines.attachTo lines

        selection = $("<div>").addClass("selection")
        @$node.append selection

        # Add product selection
        productSelection = $("<div>").addClass "productSelection"
        selection.append productSelection
        ProductSelection.attachTo productSelection, @attr

        # Add time selection
        timeSelection = $("<div>").addClass "timeSelection"
        selection.append timeSelection
        TimeSelection.attachTo timeSelection, @attr

        # Add timezone selection
        timezoneSelection = $("<div>").addClass "timezoneSelection"
        selection.append timezoneSelection
        TimeZoneSelection.attachTo timezoneSelection, @attr

        # Add number format selection
        numberFormatSelection = $("<div>").addClass "numberFormatSelection"
        selection.append numberFormatSelection
        NumberFormatSelection.attachTo numberFormatSelection, @attr

        # Add download button
        downloadLink = $("<a>")
          .addClass("downloadLink")
        downloadButton = $("<button>")
          .addClass("btn")
          .addClass("download")
          .text("Download")
        @$node.append($("<div>")
          .append(downloadLink
            .append(downloadButton)))

        @getMsg().showText "Loading..."

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
          @trigger @select("timezoneSelection"), "refresh", data: data
          @trigger @select("numberFormatSelection"), "refresh", data: data

