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

  describe "Scales", ->

    Scale = null

    before (done) ->
      require ["scale"], (s) ->
        Scale = s
        done()

    dataMock = [
      [{x: -5, y: -5}, {x: 0, y: 0}, {x: 5, y: 5}]
      [{x: 50, y: 30}, {x: -20, y: 0}, {x: 5, y: 12}]
    ]
    optionsMock =
      width: 500
      height: 500
      scale:
        x:
          type: "linear"
        y:
          type: "linear"

    chartMock =
      options: optionsMock
      data: dataMock

    isWithin = (relativeError, expected, value) ->
      error = Math.abs relativeError * expected
      value >= expected - error and
        value <= expected + error

    it "Should setup linear scales with the right domain", ->
      chart = $.extend true, {}, chartMock
      Scale.init chart
      expect(chart.xScale).exists
      expect(chart.yScale).exists

      expect(chart.xScale.range()).deep.equals [0, optionsMock.width]
      expect(chart.yScale.range()).deep.equals [optionsMock.height, 0]

      minX = _.chain(dataMock.map (dps) -> dps.map (dp) -> dp.x).flatten().min().value()
      maxX = _.chain(dataMock.map (dps) -> dps.map (dp) -> dp.x).flatten().max().value()
      expect(chart.xScale.domain()).to.have.length 2
      expect(isWithin 1, minX, chart.xScale.domain()[0]).to.be.true
      expect(isWithin 1, maxX, chart.xScale.domain()[1]).to.be.true

      minY = _.chain(dataMock.map (dps) -> dps.map (dp) -> dp.y).flatten().min().value()
      maxY = _.chain(dataMock.map (dps) -> dps.map (dp) -> dp.y).flatten().max().value()
      expect(chart.yScale.domain()).to.have.length 2
      console.log minY
      expect(isWithin 1, minY, chart.yScale.domain()[0]).to.be.true
      expect(isWithin 1, maxY, chart.yScale.domain()[1]).to.be.true

    it "Should setup an ordinal x scale with the right domain", ->
      chart = $.extend true, {}, chartMock
      chart.options.scale.x.type = "ordinal"
      Scale.init chart

      expect(chart.xScale).exists
      expectedDomain = _.chain(dataMock.map (dps) -> dps.map (dp) -> dp.x).flatten().uniq().value()
      expect(chart.xScale.domain()).deep.equals expectedDomain

    it "Should setup a fixed linear x scale", ->
      chart = $.extend true, {}, chartMock
      chart.options.scale.x.type = "linear"
      chart.options.scale.x.domain =
        type: "fixed"
        low: 10
        high: 50

      Scale.init chart
      expect(chart.xScale).exists
      expect(chart.xScale.domain()).deep.equals [10, 50]

describe "DOM tests", ->
  ChartManager = null

  before (done) ->
    $("body").prepend $("<div>").addClass("chart").attr("data-chart-id", 0)

    require ["chart_manager"], (c) ->
      ChartManager = c
      done()


  it "Should setup the chart div", ->
    $chart = $(".chart")
    expect($chart.length).equals 1
