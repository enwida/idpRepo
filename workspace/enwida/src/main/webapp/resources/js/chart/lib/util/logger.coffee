define ->

  class Logger

    levels: ["ERROR", "WARNING", "DEBUG"]
    envLevels:
      debug: 2
      testing: 1
      production: 0

    constructor: (@env="production") ->

    isLogLevel: (level) ->
      level <= @envLevels[@env]

    # log: [component] -> [level] -> msg -> ()
    log: (component, level, msg) ->
      # Handle optional parameters
      if arguments.length == 1
        msg = component
        component = null
        level = 2
      else if arguments.length == 2
        msg = level
        if typeof component is "number"
          level = component
          component = null
        else
          level = 2

      if @isLogLevel level
        componentMsg = if component? then "#{component}: " else ""
        console.log "[#{@levels[level]}] #{componentMsg}#{msg}"

    logCurried: (component) => (level) => (msg) => @log component, level, msg

    logError: (component) => (msg) => @log component, 0, msg
    logWarning: (component) => (msg) => @log component, 1, msg
    logDebug: (component) => (msg) => @log component, 2, msg

  # Exported function
  init: (env) -> new Logger env
