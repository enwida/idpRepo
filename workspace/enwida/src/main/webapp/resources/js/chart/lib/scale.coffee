define ->

  init: (@chart) ->
    @options = @chart.options["scale"]
    @setupXScale()
    @setupYScale()
    @setupDomain @chart.xScale, "x"
    @setupDomain @chart.yScale, "y"
    @chart.xScale.nice?()
    @chart.yScale.nice?()

  setupXScale: ->
    unless @options && @options["x"] && @options["x"]["type"]
      @setupXScaleLinear()
      return

    switch @options["x"]["type"]
      when "linear" then @setupXScaleLinear()
      when "ordinal" then @setupXScaleOrdinal()
      when "date" then @setupXScaleDate()

  setupYScale: ->
    @setupYScaleLinear()

  setupXScaleLinear: ->
    @chart.xScale = d3.scale.linear().range [0, @chart.options.width]

  setupXScaleOrdinal: ->
    # Implies a mapped domain type
    @options["x"]["domain"] ?= type: "map"
    padding = @options.x.padding ? 0.1
    @chart.xScale = d3.scale.ordinal().rangeRoundBands [0, @chart.options.width], padding

  setupXScaleDate: ->
    @chart.xScale = d3.time.scale().range [0, @chart.options.width]
    @chart.xScale.tickFormat = (count) =>
      formats = @options?.x?.dateFormats ? [
        ["%Y", "%Y-%m-%d"]
        ["%m", "%b"]
        ["%d", "%d"]
        ["%H:%M", "%H:%M"]
      ]

      # Closure variable containing the last tick
      last = null

      (date) ->
        # Helper function
        take = (format) ->
          last = date
          d3.time.format(format[1]) date

        # Take the first format for the first tick
        unless last?
          return take formats[0]

        # Take the first format which changed since the last tick
        for format in formats
          f = d3.time.format format[0]
          if f(date) isnt f(last)
            return take format

        # Take the last format otherwise
        return take formats[formats.length - 1]

  setupYScaleLinear: ->
    @chart.yScale = d3.scale.linear().range [@chart.options.height, 0]

  setupDomain: (scale, key) ->
    unless @options? && @options[key]? && @options[key]["domain"]? && @options[key]["domain"]
      @setupDomainExtent scale, key
      return

    strategy = @options[key]["domain"]["type"]
    if strategy instanceof Array
      for s in strategy
        @applyStrategy s, scale, key
    else
      @applyStrategy strategy, scale, key

  applyStrategy: (strategy, scale, key) ->
    switch strategy
      when "extent" then @setupDomainExtent scale, key
      when "map" then @setupDomainMap scale, key
      when "stretch" then @setupDomainStretch scale
      when "fixed" then @setupDomainFixed scale, key
      else throw new Error "Not a valid scale type: #{strategy}"

  setupDomainExtent: (scale, key) ->
    scale.domain(d3.extent @allValues(key))

  setupDomainStretch: (scale, minStretch=0.9, maxStretch=1.1) ->
    domain = scale.domain()
    scale.domain [domain[0] * minStretch, domain[1] * maxStretch]

  setupDomainMap: (scale, key) ->
    scale.domain(@allValues key)

  setupDomainFixed: (scale, key) ->
    low = @getBound @options[key]["domain"]["low"], key
    high = @getBound @options[key]["domain"]["high"], key
    scale.domain [low, high]

  allValues: (key) ->
    allValues = []
    for lineData in @chart.data
      for dataPoint in lineData
        allValues.push dataPoint[key]
    allValues

  getBound: (bound, key) ->
    return bound if $.isNumeric bound
    switch bound
      when "min" then d3.min @chart.data, (d) -> d[key]
      when "max" then d3.max @chart.data, (d) -> d[key]
      else throw new Error "Not a valid bound: #{bound}"
