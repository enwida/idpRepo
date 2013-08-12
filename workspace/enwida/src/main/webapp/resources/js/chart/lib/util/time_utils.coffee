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

