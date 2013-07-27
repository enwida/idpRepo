define ->

  id = (a) -> a

  transformers:
    minmax: (lines) ->
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
      result

    carpet: (lines) ->
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

      [line]

    posneg: (lines) ->
      # Calculate negative line
      for dp in lines[1].dataPoints
        dp.y *= -1

      # Strip remaining lines
      [lines[0], lines[1]]

  transform: (type, lines) ->
    transformer = @transformers[type] ? id
    transformer lines
