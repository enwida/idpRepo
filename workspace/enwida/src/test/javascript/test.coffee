describe "System Test", ->

  it "Sanity checks", ->
    expect("me").to.exist
    expect(null).not.to.exist

  it "Require check", (done) ->
    expect(require).to.exist

    require ["generic_chart"], (GenericChart) ->
      expect(GenericChart).to.exist
      done()

describe "Unit tests", ->

  describe "Resolution calculation", (desc) ->

    Resolution = null

    before (done) ->
      require ["resolution"], (r) ->
        Resolution = r
        done()

    it "Should always return hourly resolution for carpet plots", ->
      res = Resolution.getOptimalResolution "carpet"
      expect(res).equals "HOURLY"

    describe "Sample tests", ->

      allResoultions = ["QUATER_HOURLY", "HOURLY", "DAILY", "WEEKLY", "MONTHLY", "YEARLY"]
      chartTypes = ["line", "bar", "minmax", "posneg"]
      chartWidth = 800

      testSamples = (sampleTests) ->
        sampleTests.forEach (sampleTest) ->
          it "Should return #{sampleTest.result} for #{sampleTest.argsDesc}", ->
            res = Resolution.getOptimalResolution sampleTest.args...
            expect(res).equals sampleTest.result

      describe "One year time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2011, 1, 1
        expected = "MONTHLY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "one year #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests

      describe "One month time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2010, 1, 1
        expected = "DAILY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "one month #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests

      describe "One week time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2010, 0, 8
        expected = "DAILY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "one week #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests
        
      describe "One day time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2010, 0, 2
        expected = "HOURLY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "one day #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests

      describe "8 hours time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2010, 0, 1, 8
        expected = "QUATER_HOURLY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "8 hours #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests

      describe "2 months time span", ->
        timeSpan =
          from: new Date 2010, 0, 1
          to:   new Date 2010, 3, 0
        expected = "WEEKLY"

        sampleTests = chartTypes.map (chartType) ->
          argsDesc: "2 months #{chartType} chart"
          args: [chartType, timeSpan, allResoultions, chartWidth]
          result: expected

        testSamples sampleTests

      describe "Special checks", ->
        sampleTests = [
          {
            argsDesc: "one year in a bigger line chart"
            args: ["line", { from: new Date(2010, 0, 1), to: new Date(2011, 0, 1) }, allResoultions, 1200]
            result: "WEEKLY"
          }
          {
            argsDesc: "one month in a bar chart with 3 lines"
            args: ["bar", { from: new Date(2010, 0, 1), to: new Date(2010, 1, 1) }, allResoultions, 1200, 3]
            result: "WEEKLY"
          }
          {
            argsDesc: "one month in a minmax chart with 3 lines (number of lines don't matter)"
            args: ["minmax", { from: new Date(2010, 0, 1), to: new Date(2010, 1, 1) }, allResoultions, 1200, 3]
            result: "DAILY"
          }
        ]

        testSamples sampleTests

