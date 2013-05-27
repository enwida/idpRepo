(function(){define("scale",[],function(){return{init:function(e){var t,n;return this.chart=e,this.options=this.chart.options.scale,this.setupXScale(),this.setupYScale(),this.setupDomain(this.chart.xScale,"x"),this.setupDomain(this.chart.yScale,"y"),typeof (t=this.chart.xScale).nice=="function"&&t.nice(),typeof (n=this.chart.yScale).nice=="function"?n.nice():void 0},setupXScale:function(){if(!(this.options&&this.options.x&&this.options.x.type)){this.setupXScaleLinear();return}switch(this.options.x.type){case"linear":return this.setupXScaleLinear();case"ordinal":return this.setupXScaleOrdinal();case"date":return this.setupXScaleDate()}},setupYScale:function(){return this.setupYScaleLinear()},setupXScaleLinear:function(){return this.chart.xScale=d3.scale.linear().range([0,this.chart.options.width])},setupXScaleOrdinal:function(){var e,t,n,r;return(n=(t=this.options["x"])["domain"])==null&&(t.domain={type:"map"}),e=(r=this.options.x.padding)!=null?r:.1,this.chart.xScale=d3.scale.ordinal().rangeRoundBands([0,this.chart.options.width],e)},setupXScaleDate:function(){var e=this;return this.chart.xScale=d3.time.scale().range([0,this.chart.options.width]),this.chart.xScale.tickFormat=function(t){var n,r,i,s,o;return n=(i=(s=e.options)!=null?(o=s.x)!=null?o.dateFormats:void 0:void 0)!=null?i:[["%Y","%Y-%m-%d"],["%m","%b"],["%d","%d"],["%H:%M","%H:%M"]],r=null,function(e){var t,i,s,o,u;s=function(t){return r=e,d3.time.format(t[1])(e)};if(r==null)return s(n[0]);for(o=0,u=n.length;o<u;o++){i=n[o],t=d3.time.format(i[0]);if(t(e)!==t(r))return s(i)}return s(n[n.length-1])}}},setupYScaleLinear:function(){return this.chart.yScale=d3.scale.linear().range([this.chart.options.height,0])},setupDomain:function(e,t){var n,r,i,s,o;if(this.options==null||this.options[t]==null||this.options[t]["domain"]==null||!this.options[t].domain){this.setupDomainExtent(e,t);return}r=this.options[t].domain.type;if(r instanceof Array){o=[];for(i=0,s=r.length;i<s;i++)n=r[i],o.push(this.applyStrategy(n,e,t));return o}return this.applyStrategy(r,e,t)},applyStrategy:function(e,t,n){switch(e){case"extent":return this.setupDomainExtent(t,n);case"map":return this.setupDomainMap(t,n);case"stretch":return this.setupDomainStretch(t);case"fixed":return this.setupDomainFixed(t,n);default:throw new Error("Not a valid scale type: "+e)}},setupDomainExtent:function(e,t){return e.domain(d3.extent(this.allValues(t)))},setupDomainStretch:function(e,t,n){var r;return t==null&&(t=.9),n==null&&(n=1.1),r=e.domain(),e.domain([r[0]*t,r[1]*n])},setupDomainMap:function(e,t){return e.domain(this.allValues(t))},setupDomainFixed:function(e,t){var n,r;return r=this.getBound(this.options[t].domain.low,t),n=this.getBound(this.options[t].domain.high,t),e.domain([r,n])},allValues:function(e){var t,n,r,i,s,o,u,a;t=[],a=this.chart.data;for(i=0,o=a.length;i<o;i++){r=a[i];for(s=0,u=r.length;s<u;s++)n=r[s],t.push(n[e])}return t},getBound:function(e,t){if($.isNumeric(e))return e;switch(e){case"min":return d3.min(this.chart.data,function(e){return e[t]});case"max":return d3.max(this.chart.data,function(e){return e[t]});default:throw new Error("Not a valid bound: "+e)}}}})}).call(this),function(){define("generic_chart",["scale"],function(e){var t;return t=function(){function t(t){var n,r,i;if(t==null||t.data==null)throw"No data given";n={margin:{top:20,left:50,right:20,bottom:30},width:960,height:500,parent:"body"},this.options=$.extend(n,t),this.chartData=t.data,this.metaData=this.chartData.metaData,this.xLabel=(r=this.metaData.hAxisLabel)!=null?r:this.chartData.allDataLines[0].xTitle,this.yLabel=(i=this.metaData.vAxisLabel)!=null?i:this.chartData.allDataLines[0].yTitle,this.lineLabels=this.chartData.allDataLines.map(function(e){return e.yTitle}),this.data=this.chartData.allDataLines.map(function(e){return e.dataPoints}),e.init(this),this.generateAxes()}return t.prototype.generateXAxis=function(){return this.xAxis=d3.svg.axis().scale(this.xScale).orient("bottom")},t.prototype.generateYAxis=function(){return this.yAxis=d3.svg.axis().scale(this.yScale).orient("left")},t.prototype.generateAxes=function(e,t){return this.generateXAxis(),this.generateYAxis()},t.prototype.drawSvg=function(){var e,t;if(this.svg!=null)return;return t=this.options.width+this.options.margin.left+this.options.margin.right,e=this.options.height+this.options.margin.top+this.options.margin.bottom,this.svg=d3.select(this.options.parent).append("svg").attr("width",t).attr("height",e).append("g").attr("transform","translate("+this.options.margin.left+","+this.options.margin.top+")")},t.prototype.drawXAxis=function(e){return this.svg.append("g").attr("class","x axis").attr("transform","translate(0,"+this.options.height+")").call(e).append("text").attr("x",this.options.width).attr("y",-5).attr("text-anchor","end").text(""+this.xLabel)},t.prototype.drawYAxis=function(e){return this.svg.append("g").attr("class","y axis").call(e).append("text").attr("transform","rotate(-90)").attr("y",12).attr("text-anchor","end").text(""+this.yLabel)},t.prototype.drawLegend=function(){return this.legend=this.svg.append("g").attr("class","legend").attr("transform","translate(50,30)").attr("data-style-padding",10).call(d3.legend)},t.prototype.drawAxes=function(){return this.drawXAxis(this.xAxis),this.drawYAxis(this.yAxis)},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("line_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.generateLine=function(){var e=this;return d3.svg.line().x(function(t){return e.chart.xScale(t.x)}).y(function(t){return e.chart.yScale(t.y)})},t.prototype.drawLine=function(e,t){var n;return t==null&&(t=0),n=this.generateLine(),this.chart.svg.append("path").datum(e).attr("class","line line"+t).attr("data-legend",this.chart.lineLabels[t]).attr("d",n)},t.prototype.drawDots=function(e,t){var n=this;return t==null&&(t=0),this.chart.svg.selectAll("dot").data(e).enter().append("circle").attr("class","dot dot"+t).attr("r",4.5).attr("cx",function(e){return n.chart.xScale(e.x)}).attr("cy",function(e){return n.chart.yScale(e.y)}).attr("original-title",function(e){var t,r,i,s;return t=e.x,((r=n.chart.options)!=null?(i=r.scale)!=null?(s=i.x)!=null?s.type:void 0:void 0:void 0)==="date"&&(t=d3.time.format("%Y-%m-%d %H:%M")(new Date(t))),""+n.chart.xLabel+": "+t+" "+n.chart.yLabel+": "+e.y})},t.prototype.draw=function(){var e,t,n,r,i;this.chart.drawSvg(),this.chart.drawAxes(),i=this.chart.data;for(e=n=0,r=i.length;n<r;e=++n)t=i[e],this.drawLine(t,e),this.drawDots(t,e);return this.chart.drawLegend()},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("bar_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.drawBars=function(e){var t,n,r,i,s=this;return i=this.chart.options.scale,r=i&&i.x&&i.x.type==="ordinal",r?(n=this.chart.xScale.rangeBand(),t=0):(n=this.chart.xScale(this.minInterval("x"))-this.chart.xScale(0),n*=.9,t=n/2),this.chart.svg.selectAll(".bar").data(e).enter().append("rect").attr("class","bar").attr("x",function(e){return s.chart.xScale(e.x)-t}).attr("y",function(e){return s.chart.yScale(e.y)}).attr("width",n).attr("height",function(e){return s.chart.options.height-s.chart.yScale(e.y)}).attr("fill",function(e){return"#0000aa"})},t.prototype.minInterval=function(e){var t,n,r,i,s;s=null,i=this.chart.data[0][e],r=1;while(r<this.chart.data.length){t=this.chart.data[r][e],n=t-i;if(!s||s>n)s=n;i=t,r+=1}return s},t.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.drawBars(this.chart.data[0])},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("carpet_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.drawCarpet=function(e){var t,n,r,i,s,o=this;return i=this.chart.xScale.domain(),s=this.chart.yScale.domain(),r=this.chart.options.width/(i[1]-i[0]+1),n=this.chart.options.height/(s[1]-s[0]+1)+1,t=d3.scale.category20c(),this.chart.svg.selectAll(".carpet").data(e).enter().append("rect").attr("class","carpet").attr("x",function(e){return o.chart.xScale(e.x)}).attr("y",function(e){return o.chart.yScale(e.y)-n}).attr("fill",function(e){return t(e.v)}).attr("width",r).attr("height",n)},t.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.drawCarpet(this.chart.data[0])},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){require.config({baseUrl:"/enwida/resources/js/chart/lib"}),require(["line_chart","bar_chart","carpet_chart"],function(e,t,n){var r,i,s;return s="json?type=rl_ab1&product=210&startTime=2010-12-30&endTime=2010-12-31&resolution=HOURLY",r=function(t){var n;return $("#chart svg").remove(),$("#chart h3").text(t.metaData.chartTitle),n=e.init({parent:"#chart",data:t,scale:{x:{type:"date"}}}),n.draw(),$("circle").tipsy({gravity:"s"})},i=function(e){return $.ajax(e,{success:r,error:function(e){return alert(e)}})},$(document).ready(function(){return $("#url").keyup(function(e){if(e.which!==13)return;return i($("#url").val())}),$("#templates a").click(function(e){return $("#url").val($(e.target).text()),i($(e.target).text())}),$("#url").val(s),i(s)})})}.call(this),define("../charts",function(){});