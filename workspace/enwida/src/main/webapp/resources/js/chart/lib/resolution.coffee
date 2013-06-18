define ->

  resolutions =
    "QUATER_HOURLY" : 60*15
    "HOURLY"        : 60*60
    "DAILY"         : 60*60*24
    "WEEKLY"        : 60*60*24*7   # roundabout
    "MONTHLY"       : 60*60*24*30  # roundabout
    "YEARLY"        : 60*60*24*365 # roundabout

  optimalDensity = 25
  maximumDensity = 15

  getOptimalResolution: (timeRange, filters, width) ->
    optimalDataPointCount = width / optimalDensity
    maximumDataPointCount = width / maximumDensity

    diffSeconds = (timeRange.to - timeRange.from) / 1000
    validKeys = _(_(resolutions).keys()).filter (res) -> _(filters).contains res
    counts = validKeys.map (key) -> [key, diffSeconds / resolutions[key]]

    # Find optimal resolution
    best = counts[0]
    for i in [1...counts.length]
      bestDiff = Math.abs(best[1] - optimalDataPointCount)
      currentDiff = Math.abs(counts[i][1] - optimalDataPointCount)

      if best[1] > maximumDataPointCount
        # Current best doesn't obey maximum restriction
        if counts[i][1] <= maximumDataPointCount or currentDiff < bestDiff
          best = counts[i]
      else if currentDiff < bestDiff and counts[i][1] < maximumDataPointCount
          best = counts[i]

    best[0]
