define ->

  modifyDate: (base, opts, backwards) ->
    result = new Date base

    days = parseInt opts.days
    months = parseInt opts.months
    years = parseInt opts.years

    if backwards ? opts.backwards
      days *= -1
      months *= -1
      years *= -1

    unless isNaN(days)
      result.setDate(result.getDate() + days)
    unless isNaN(months)
      result.setMonth(result.getMonth() + months)
    unless isNaN(years)
      result.setFullYear(result.getFullYear() + years)

    result

  getDateModifier: (timeRange) ->
    switch timeRange
      when "Day" then days: 1
      when "Week" then days: 7
      when "Month" then months: 1
      when "Year" then years: 1
      else {}

  normalizeDate: (time, timeRange) ->
    result = new Date time
    switch timeRange
      when "Week"
        result = @getWeekStart result
      when "Month"
        result.setDate 1
      when "Year"
        result.setMonth 0
        result.setDate 1
    result

  addRange: (time, timeRange) ->
    modifier = @getDateModifier timeRange
    @modifyDate time, modifier

  subtractRange: (time, timeRange) ->
    modifier = @getDateModifier timeRange
    modifier.backwards = true
    @modifyDate time, modifier

  getWeekStart: (date) ->
    result = new Date date
    while result.getDay() isnt 1
      result.setDate (result.getDate() - 1)
    result

  timeRangeLiteral: (timeRange) ->
    diff = timeRange.to - timeRange.from
    if diff < 1000*60*60*24*7 then "Day"
    else if diff < 1000*60*60*24*28 then "Week"
    else if diff < 1000*60*60*24*365 then "Month"
    else "Year"

  timeRangePrototype: (timeRange) ->
    time = new Date()
    from : time
    to   : @addRange time, timeRange

  dataSetCount: (timeRange, resolution) ->
    # Normalize time range
    timeRange.from = new Date timeRange.from
    timeRange.to   = new Date timeRange.to

    diff = timeRange.to - timeRange.from
    switch resolution
      when "QUATER_HOURLY" then diff / (15*60*1000)
      when "HOURLY"        then diff / (60*60*1000)
      when "DAILY"         then diff / (24*60*60*1000)
      when "WEEKLY"        then diff / (7*24*60*60*1000)
      when "MONTHLY"
        years = timeRange.to.getFullYear() - timeRange.from.getFullYear()
        months = timeRange.to.getMonth() - timeRange.from.getMonth()
        years * 12 + months
      when "YEARLY"
        timeRange.to.getFullYear() - timeRange.from.getFullYear()

  asUTC: (date) ->
    new Date date.getTime() + date.getTimezoneOffset() * 60 * 1000

  fromUTC: (date) ->
    new Date date.getTime() - date.getTimezoneOffset() * 60 * 1000

  isInTimeRange: (date, timeRange) ->
    date >= timeRange.from and date <= timeRange.to

  isTimeRangeInside: (testee, timeRange) ->
    @isInTimeRange(testee.from, timeRange) and @isInTimeRange(testee.to, timeRange)

  nearestInTimeRange: (date, timeRange) ->
    from = new Date timeRange.from
    to   = new Date timeRange.to

    new Date \
      if date < from
        from
      else
        if date <= to then date else to

  normalizedToTimeRange: (date, timeRange) ->
    date = new Date date
    date.setMilliseconds 0
    date.setSeconds 0
    date.setMinutes 0
    date.setHours 0

    switch timeRange
      when "Month"
        date.setDate 1
      when "Year"
        date.setDate 1
        date.setMonth 0

    date
