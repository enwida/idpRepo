// Generated by CoffeeScript 1.4.0
(function() {

  define(["generic_chart"], function(generic_chart) {
    var CarpetChart;
    CarpetChart = (function() {

      function CarpetChart(options) {
        this.chart = generic_chart.init(options);
      }

      CarpetChart.prototype.drawCarpet = function(data) {
        var color, rectHeight, rectWidth, xDomain, yDomain,
          _this = this;
        xDomain = this.chart.xScale.domain();
        yDomain = this.chart.yScale.domain();
        rectWidth = this.chart.options.width / (xDomain[1] - xDomain[0] + 1);
        rectHeight = this.chart.options.height / (yDomain[1] - yDomain[0] + 1) + 1;
        color = d3.scale.category20c();
        return this.chart.svg.selectAll(".carpet").data(data).enter().append("rect").attr("class", "carpet").attr("x", function(d) {
          return _this.chart.xScale(d.x);
        }).attr("y", function(d) {
          return _this.chart.yScale(d.y) - rectHeight;
        }).attr("fill", function(d) {
          return color(d.v);
        }).attr("width", rectWidth).attr("height", rectHeight);
      };

      CarpetChart.prototype.draw = function() {
        this.chart.drawSvg();
        this.chart.drawAxes();
        return this.drawCarpet(data[0]);
      };

      CarpetChart.prototype.redraw = function() {
        this.chart.svg = null;
        return this.draw();
      };

      return CarpetChart;

    })();
    return {
      init: function(options) {
        return new CarpetChart(options);
      }
    };
  });

}).call(this);
