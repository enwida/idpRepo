define ->

  resolutions =
    "QUATER_HOURLY" : 60*15
    "HOURLY"        : 60*60
    "DAILY"         : 60*60*24
    "WEEKLY"        : 60*60*24*7   # roundabout
    "MONTHLY"       : 60*60*24*30  # roundabout
    "YEARLY"        : 60*60*24*365 # roundabout

  optimalDataPointCount = 30

  getOptimalResolution: (timeRange, filters) ->
    # Sanatize time range
    timeRange.from = new Date timeRange.from unless typeof timeRange.from is "object"
    timeRange.to = new Date timeRange.to unless typeof timeRange.to is "object"

    diffSeconds = (timeRange.to - timeRange.from) / 1000
    validKeys = _(_(resolutions).keys()).filter (res) -> _(filters).contains res
    counts = validKeys.map (key) -> [key, diffSeconds / resolutions[key]]

    # Find optimal resolution
    best = counts[0]
    for i in [1...counts.length]
      bestDiff = Math.abs(best[1] - optimalDataPointCount)
      currentDiff = Math.abs(counts[i][1] - optimalDataPointCount)
      if currentDiff < bestDiff
        best = counts[i]

    best[0]
