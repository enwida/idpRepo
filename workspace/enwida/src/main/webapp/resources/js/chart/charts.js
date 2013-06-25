(function(){define("resolution",[],function(){var e,t,n;return n={QUATER_HOURLY:900,HOURLY:3600,DAILY:86400,WEEKLY:604800,MONTHLY:2592e3,YEARLY:31536e3},t=25,e=15,{getOptimalResolution:function(r,i,s){var o,u,a,f,l,c,h,p,d,v,m;p=s/t,h=s/e,l=(r.to-r.from)/1e3,d=_(_(n).keys()).filter(function(e){return _(i).contains(e)}),a=d.map(function(e){return[e,l/n[e]]}),o=a[0];for(c=v=1,m=a.length;1<=m?v<m:v>m;c=1<=m?++v:--v){u=Math.abs(o[1]-p),f=Math.abs(a[c][1]-p);if(o[1]>h){if(a[c][1]<=h||f<u)o=a[c]}else f<u&&a[c][1]<h&&(o=a[c])}return o[0]}}})}).call(this),function(){define("navigation",["resolution"],function(e){return flight.component(function(){return this.productParts=["type","posneg","timeslot"],this.treeParts=["type","timeslot","posneg"],this.dateFormat=d3.time.format("%Y-%m-%d"),this.datePickerFormat="yyyy-mm-dd",this.timeRanges=["Day","Week","Month","Year"],this.viewModes={Day:"days",Week:"days",Month:"months",Year:"years"},this.createElements=function(){var e,t,n,r,i,s,o,u=this;n=$("<div>").addClass("productSelect").append($("<select>").addClass("tso").change(function(){return u.trigger("updateProducts")})),t=$("<div>").addClass("product").css("display","inline"),o=this.productParts;for(i=0,s=o.length;i<s;i++)e=o[i],t.append($("<select>").addClass(e).change(function(){return u.trigger("updateProducts")}));return n.append(t),r=$("<div>").addClass("timeselect").append($("<div>").addClass("input-append").append($("<input>").attr("type","text").addClass("from").attr("readonly",!0)).append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon").append($("<i>").addClass("icon-th")))).append($("<select>").addClass("timerange")).change(function(){return u.refreshDatepicker()}),this.$node.append(n),this.$node.append(r)},this.getNavigationData=function(e){var t=this;return $.ajax("navigation.test",{data:{chartId:this.attr.id},success:function(n){return t.navigationData=n,e(null,n)},error:function(t){return e(t)}})},this.refresh=function(){var e=this;return this.getNavigationData(function(t,n){var r,i,s;if(t!=null)throw t;return r={from:new Date(n.timeRangeMax.from),to:new Date(n.timeRangeMax.to)},i=(s=n.datepickerViewMode)!=null?s:"months",e.fillTso(),e.fillProduct(),e.fillResolutions(),e.fillTimeRange(i,r),e.setDefaults(n.defaults),e.trigger("updateNavigation",{data:n}),e.triggerGetLines()})},this.fillTso=function(){var e,t,n,r;e=this.select("tso"),e.empty(),n=this.navigationData.tsos,r=[];for(t in this.navigationData.tsos)r.push(e.append($("<option>").val(t).text(n[t])));return r},this.fillProduct=function(){return this.setProduct(111)},this.fillResolutions=function(){var e,t,n,r,i,s;e=this.select("resolution"),e.empty(),i=this.navigationData.allResolutions,s=[];for(n=0,r=i.length;n<r;n++)t=i[n],s.push(e.append($("<option>").val(t).text(t)));return s},this.fillTimeRange=function(e,t){var n,r,i,s,o,u,a,f,l=this;e==null&&(e="days"),t==null&&(t={}),this.select("from").datepicker({format:this.datePickerFormat,viewMode:e,minViewMode:e,startDate:(o=t.from)!=null?o:"1900-01-01",endDate:(u=t.to)!=null?u:new Date}),this.select("fromIcon").click(function(){return l.select("from").datepicker("show")}),n=this.select("timeRange"),a=this.timeRanges,f=[];for(i=0,s=a.length;i<s;i++)r=a[i],f.push(n.append($("<option>").val(r).text(r)));return f},this.refreshDatepicker=function(){switch(this.select("timeRange").val()){case"Day":return this.setDateRangeViewMode(0);case"Week":return this.setDateRangeViewMode(0);case"Month":return this.setDateRangeViewMode(1);case"Year":return this.setDateRangeViewMode(2)}},this.setDateRangeViewMode=function(e){return console.log("Settings mode to "+e),this.select("from").data("datepicker").minViewMode=e,this.select("from").data("datepicker").startViewMode=e,this.select("from").data("datepicker").viewMode=e,this.select("from").datepicker("show"),this.select("from").datepicker("hide")},this.setDefaults=function(e){return this.select("tso").val(e.tso),this.setProduct(e.product),this.select("resolution").val(e.resolution),this.select("from").datepicker("update",new Date(e.timeRange.from))},this.getProductTree=function(){var e;return e=parseInt(this.select("tso").val()),_.find(this.navigationData.productTrees,function(t){return t.tso===e})},this.setProduct=function(e){var t,n,r,i,s,o,u,a,f,l,c,h,p,d,v,m,g;e=""+e,u={};for(r=a=0,p=this.productParts.length;0<=p?a<p:a>p;r=0<=p?++a:--a)i=parseInt(e.substring(r,r+1)),u[this.productParts[r]]=i;o=this.getProductTree().root,d=this.treeParts,g=[];for(f=0,c=d.length;f<c;f++){s=d[f],n=this.select("product").find("."+s),n.empty(),v=o.children;for(l=0,h=v.length;l<h;l++)t=v[l],n.append($("<option>").val(t.id).text(t.name));i=u[s],n.val(i),g.push(o=(m=_.find(o.children,function(e){return e.id===i}))!=null?m:o.children[0])}return g},this.getProduct=function(){var e,t,n,r,i,s;n="",s=this.productParts;for(r=0,i=s.length;r<i;r++)t=s[r],e=this.select("product").find("."+t),n+=e.val();return n},this.updateProducts=function(){return this.setProduct(this.getProduct())},this.getDateTo=function(e){var t;t=new Date(e);switch(this.select("timeRange").val()){case"Day":t.setDate(t.getDate()+1);break;case"Week":t.setDate(t.getDate()+7);break;case"Month":t.setMonth(t.getMonth()+1);break;case"Year":t.setFullYear(t.getFullYear()+1)}return t},this.triggerGetLines=function(t){var n,r,i,s;return t==null&&(t={}),n=(s=t.from)!=null?s:new Date(this.select("from").val()),i={from:n,to:this.getDateTo(n)},r=e.getOptimalResolution(i,this.navigationData.allResolutions,this.attr.width),this.trigger("getLines",{tso:this.select("tso").val(),product:this.getProduct(),timeRange:i,resolution:r})},this.setupEvents=function(){var e=this;return this.$node.on("change",function(t){return e.triggerGetLines()})},this.defaultAttrs({tso:".tso",product:".product",resolution:".resolution",from:".from",fromIcon:".fromIcon",timeRange:".timerange"}),this.after("initialize",function(){return this.on("refresh",this.refresh),this.on("updateProducts",this.updateProducts),this.createElements(),this.refresh(),this.setupEvents()})})})}.call(this),function(){define("spreadsheet",[],function(){var e;return e=function(){function e(e,t){this.isDateScale=t!=null?t:!1,this.element=$(e)}return e.prototype.draw=function(e){var t;return this.element.empty(),t=$("<table>"),t.append(this.getTableHeader(e)),this.element.append(t),this.addTableRows(t,e)},e.prototype.getTableHeader=function(e,t){var n,r,i,s,o;t==null&&(t="Time"),r=$("<tr>"),r.append($("<th>").text(t)),o=[];for(i=0,s=e.length;i<s;i++)n=e[i],console.log(n),o.push(r.append($("<th>").text(n.title)));return o},e.prototype.addTableRows=function(e,t){var n,r,i,s,o,u,a,f,l,c,h,p,d,v,m,g,y,b,w,E,S,x;console.log("datescale: "+this.isDateScale),i=d3.time.format("%Y-%m-%d %H:%M"),a={};for(l=0,d=t.length;l<d;l++){o=t[l],b=o.dataPoints;for(c=0,v=b.length;c<v;c++)s=b[c],(w=a[y=s.x])==null&&(a[y]={}),a[s.x][o.title]=s.y}E=_(a).keys().sort(),x=[];for(h=0,m=E.length;h<m;h++){f=E[h],u=$("<tr>"),n=$("<td>"),n.text(this.isDateScale?i(new Date(parseInt(f))):f),u.append(n);for(p=0,g=t.length;p<g;p++)o=t[p],r=$("<td>"),r.text((S=a[f][o.title])!=null?S:""),u.append(r);x.push(e.append(u))}return x},e}()})}.call(this),function(){define("scale",[],function(){return{init:function(e){var t,n;return this.chart=e,this.options=this.chart.options.scale,this.setupXScale(),this.setupYScale(),this.setupDomain(this.chart.xScale,"x"),this.setupDomain(this.chart.yScale,"y"),typeof (t=this.chart.xScale).nice=="function"&&t.nice(),typeof (n=this.chart.yScale).nice=="function"?n.nice():void 0},setupXScale:function(){if(!(this.options&&this.options.x&&this.options.x.type)){this.setupXScaleLinear();return}switch(this.options.x.type){case"linear":return this.setupXScaleLinear();case"ordinal":return this.setupXScaleOrdinal();case"date":return this.setupXScaleDate()}},setupYScale:function(){return this.setupYScaleLinear()},setupXScaleLinear:function(){return this.chart.xScale=d3.scale.linear().range([0,this.chart.options.width])},setupXScaleOrdinal:function(){var e,t,n,r;return(n=(t=this.options["x"])["domain"])==null&&(t.domain={type:"map"}),e=(r=this.options.x.padding)!=null?r:.1,this.chart.xScale=d3.scale.ordinal().rangeRoundBands([0,this.chart.options.width],e)},setupXScaleDate:function(){var e=this;return this.chart.xScale=d3.time.scale().range([0,this.chart.options.width]),this.chart.xScale.tickFormat=function(t){var n,r,i,s,o;return n=(i=(s=e.options)!=null?(o=s.x)!=null?o.dateFormats:void 0:void 0)!=null?i:[["%Y","%Y-%m-%d"],["%m","%b"],["%d","%d"],["%H:%M","%H:%M"]],r=null,function(e){var t,i,s,o,u;s=function(t){return r=e,d3.time.format(t[1])(e)};if(r==null)return s(n[0]);for(o=0,u=n.length;o<u;o++){i=n[o],t=d3.time.format(i[0]);if(t(e)!==t(r))return s(i)}return s(n[n.length-1])}}},setupYScaleLinear:function(){return this.chart.yScale=d3.scale.linear().range([this.chart.options.height,0])},setupDomain:function(e,t){var n,r,i,s,o;if(this.options==null||this.options[t]==null||this.options[t]["domain"]==null||!this.options[t].domain){this.setupDomainExtent(e,t);return}r=this.options[t].domain.type;if(r instanceof Array){o=[];for(i=0,s=r.length;i<s;i++)n=r[i],o.push(this.applyStrategy(n,e,t));return o}return this.applyStrategy(r,e,t)},applyStrategy:function(e,t,n){switch(e){case"extent":return this.setupDomainExtent(t,n);case"map":return this.setupDomainMap(t,n);case"stretch":return this.setupDomainStretch(t);case"fixed":return this.setupDomainFixed(t,n);default:throw new Error("Not a valid scale type: "+e)}},setupDomainExtent:function(e,t){return e.domain(d3.extent(this.allValues(t)))},setupDomainStretch:function(e,t,n){var r;return t==null&&(t=.9),n==null&&(n=1.1),r=e.domain(),e.domain([r[0]*t,r[1]*n])},setupDomainMap:function(e,t){return e.domain(this.allValues(t))},setupDomainFixed:function(e,t){var n,r;return r=this.getBound(this.options[t].domain.low,t),n=this.getBound(this.options[t].domain.high,t),e.domain([r,n])},allValues:function(e){var t,n,r,i,s,o,u,a,f;t=[],i={},f=this.chart.data;for(s=0,u=f.length;s<u;s++){r=f[s];for(o=0,a=r.length;o<a;o++)n=r[o],i[n[e]]==null&&(t.push(n[e]),i[n[e]]=!0)}return t},getBound:function(e,t){if($.isNumeric(e))return e;switch(e){case"min":return d3.min(this.chart.data,function(e){return e[t]});case"max":return d3.max(this.chart.data,function(e){return e[t]});default:throw new Error("Not a valid bound: "+e)}}}})}.call(this),function(){define("generic_chart",["scale"],function(e){var t;return t=function(){function t(t){var n,r,i;if(t==null||t.lines==null)throw"No data given";n={margin:{top:20,left:50,right:20,bottom:30},width:960,height:500,parent:"body"},this.options=$.extend(n,t),this.lines=t.lines,this.xLabel=(r=this.options.xLabel)!=null?r:"",this.yLabel=(i=this.options.yLabel)!=null?i:"",this.lineLabels=this.lines.map(function(e){return e.title}),this.data=this.lines.map(function(e){return e.dataPoints}),e.init(this),this.generateAxes()}return t.prototype.generateXAxis=function(){return this.xAxis=d3.svg.axis().scale(this.xScale).orient("bottom")},t.prototype.generateYAxis=function(){return this.yAxis=d3.svg.axis().scale(this.yScale).orient("left")},t.prototype.generateAxes=function(e,t){return this.generateXAxis(),this.generateYAxis()},t.prototype.drawSvg=function(){var e,t;if(this.svg!=null)return;return t=this.options.width+this.options.margin.left+this.options.margin.right,e=this.options.height+this.options.margin.top+this.options.margin.bottom,this.svg=d3.select(this.options.parent).append("svg").attr("width",t).attr("height",e).append("g").attr("transform","translate("+this.options.margin.left+","+this.options.margin.top+")")},t.prototype.drawXAxis=function(e){return this.svg.append("g").attr("class","x axis").attr("transform","translate(0,"+this.options.height+")").call(e).append("text").attr("x",this.options.width).attr("y",-5).attr("text-anchor","end").text(""+this.xLabel)},t.prototype.drawYAxis=function(e){return this.svg.append("g").attr("class","y axis").call(e).append("text").attr("transform","rotate(-90)").attr("y",12).attr("text-anchor","end").text(""+this.yLabel)},t.prototype.drawLegend=function(){return},t.prototype.drawAxes=function(){return this.drawXAxis(this.xAxis),this.drawYAxis(this.yAxis)},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("line_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.generateLine=function(){var e=this;return d3.svg.line().x(function(t){return e.chart.xScale(t.x)}).y(function(t){return e.chart.yScale(t.y)})},t.prototype.drawLine=function(e,t){var n;return t==null&&(t=0),n=this.generateLine(),this.chart.svg.append("path").datum(e).attr("class","line line"+t+" visual"+t).attr("data-legend",this.chart.lineLabels[t]).attr("d",n)},t.prototype.drawDots=function(e,t){var n=this;return t==null&&(t=0),this.chart.svg.selectAll("dot").data(e).enter().append("circle").attr("class","dot dot"+t+" visual"+t).attr("r",4.5).attr("cx",function(e){return n.chart.xScale(e.x)}).attr("cy",function(e){return n.chart.yScale(e.y)}).attr("original-title",function(e){var r,i,s,o;return r=e.x,((i=n.chart.options)!=null?(s=i.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(r=d3.time.format("%Y-%m-%d %H:%M")(new Date(r))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(n.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(n.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(r)))).append($("<tr>").append($("<td align='left'>").text(n.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},t.prototype.draw=function(){var e,t,n,r,i;this.chart.drawSvg(),this.chart.drawAxes(),i=this.chart.data;for(e=n=0,r=i.length;n<r;e=++n)t=i[e],this.drawLine(t,e),this.drawDots(t,e);return this.chart.drawLegend()},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("bar_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){t=$.extend(!0,{},t),t.scale.x.type==="date"&&this.makeDateScale(t.lines),t.scale.x.type="ordinal",this.chart=e.init(t)}return t.prototype.drawBars=function(e,t){var n,r,i=this;return t==null&&(t=0),console.log(t),r=this.chart.xScale.rangeBand()/e.length,n=0,this.chart.svg.selectAll(".foo").data(e[t]).enter().append("rect").attr("class","bar bar"+t+" visual"+t+" line"+t+" line").attr("x",function(e){return i.chart.xScale(e.x)-n+t*r}).attr("y",function(e){return i.chart.yScale(e.y)}).attr("width",r).attr("height",function(e){return i.chart.options.height-i.chart.yScale(e.y)}).attr("original-title",function(e){var n,r,s,o;return n=e.x,((r=i.chart.options)!=null?(s=r.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(n=d3.time.format("%Y-%m-%d %H:%M")(new Date(n))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(i.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(i.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(n)))).append($("<tr>").append($("<td align='left'>").text(i.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},t.prototype.minInterval=function(e,t){var n,r,i,s,o;t==null&&(t=0),o=null,s=this.chart.data[t][0][e],i=1;while(i<this.chart.data[t].length){n=this.chart.data[t][i][e],r=n-s;if(!o||o>r)o=r;s=n,i+=1}return o},t.prototype.makeDateScale=function(e){var t,n,r,i,s,o,u,a,f;i=d3.time.format("%m"),f=[];for(u=0,a=e.length;u<a;u++)s=e[u],f.push(function(){var e,u,a,f;a=s.dataPoints,f=[];for(r=e=0,u=a.length;e<u;r=++e)n=a[r],o=s.dataPoints[r+1],o!=null&&(t=Math.abs(o.x-n.x),t<=24192e5?i=d3.time.format("%d"):t<=922752e5?i=d3.time.format("%m"):i=d3.time.format("%Y")),f.push(n.x=i(new Date(n.x)));return f}());return f},t.prototype.draw=function(){var e,t,n,r,i,s;this.chart.drawSvg(),this.chart.drawAxes(),i=this.chart.data,s=[];for(e=n=0,r=i.length;n<r;e=++n)t=i[e],s.push(this.drawBars(this.chart.data,e));return s},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("carpet_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.drawCarpet=function(e){var t,n,r,i,s,o=this;return i=this.chart.xScale.domain(),s=this.chart.yScale.domain(),r=this.chart.options.width/i.length,n=this.chart.options.height/s.length,t=d3.scale.linear().range(["#00f","#f00"]).domain(d3.extent(e.map(function(e){return e.v}))),this.chart.svg.selectAll(".carpet").data(e).enter().append("rect").attr("class","carpet").attr("x",function(e){return o.chart.xScale(e.x)}).attr("y",function(e){return o.chart.yScale(e.y)-n}).attr("fill",function(e){return t(e.v)}).attr("width",r).attr("height",n)},t.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.drawCarpet(this.chart.data[0])},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("visual",["line_chart","bar_chart","carpet_chart"],function(e,t,n){return flight.component(function(){return this.toCarpet=function(e){return e[0].dataPoints.map(function(e){var t,n,r;return t=new Date(e.x),t=t.getMonth()+"/"+t.getDate(),n=new Date(e.x),n=n.getHours(),r=e.y,e.x=t,e.y=n,e.v=r,e}),e},this.onNavigationData=function(e,t){return this.attr.chartOptions.xLabel=t.data.xAxisLabel,this.attr.chartOptions.yLabel=t.data.yAxisLabel,this.attr.chartOptions.scale={x:{type:t.data.isDateScale?"date":"linear"}}},this.getChart=function(r){this.attr.chartOptions.lines=r;switch(this.attr.type){case"line":return e.init(this.attr.chartOptions);case"bar":return t.init(this.attr.chartOptions);case"carpet":return this.attr.chartOptions.lines=[this.toCarpet(r)[0]],this.attr.chartOptions.scale.x.type="ordinal",this.attr.chartOptions.scale.x.padding=0,n.init(this.attr.chartOptions);default:return console.log("Unknown chart type: '"+this.attr.type+"'")}},this.draw=function(e,t){var n;this.$node.empty(),n=this.getChart(t.data),n.draw();switch(this.attr.type){case"line":return this.$node.find("circle").tipsy({gravity:"sw",html:!0,opacity:.95});case"bar":return this.$node.find("rect").tipsy({gravity:"sw",html:!0,opacity:.95})}},this.defaultAttrs({ChartClass:e}),this.after("initialize",function(){return this.on("navigationData",this.onNavigationData),this.on("draw",this.draw),this.attr.chartOptions={parent:".chart[data-chart-id='"+this.attr.id+"'] .visual",width:this.attr.width}})})})}.call(this),function(){define("lines",[],function(){return flight.component(function(){var e,t;return e=function(e,t){var n;return n=e.attr("class"),n+=" "+t,e.attr("class",n)},t=function(e,t){var n;return n=e.attr("class"),n=n.replace(new RegExp("s*"+t+"s*","g"),""),e.attr("class",n)},this.updateLines=function(n,r){var i,s,o,u,a=this;return this.$node.empty(),i=$("<ul>"),this.$node.append(i),function(){u=[];for(var e=0,t=r.lines.length;0<=t?e<t:e>t;0<=t?e++:e--)u.push(e);return u}.apply(this).forEach(function(n){var s,o;return o=r.lines[n],s=$("<li>").addClass("line"+n).append($("<table>").attr("cellpadding","3").append($("<tr>").append($("<td>").append($("<div>").addClass("linesquare"))).append($("<td>").text(o.title)))),s.click(function(){return s.toggleClass("hidden"),a.trigger("toggleLine",{lineId:n})}),s.hover(function(){return a.$node.closest(".chart").find(".line"+n).each(function(){return e($(this),"selected")}),a.$node.closest(".chart").find(".dot"+n).each(function(){return e($(this),"selected")})},function(){return a.$node.closest(".chart").find(".line"+n).each(function(){return t($(this),"selected")}),a.$node.closest(".chart").find(".dot"+n).each(function(){return t($(this),"selected")})}),i.append(s)})},this.after("initialize",function(){return this.on("updateLines",this.updateLines)})})})}.call(this),function(){define("chart_manager",["navigation","spreadsheet","visual","lines"],function(e,t,n,r){return flight.component(function(){return this.getLines=function(e,t){var n,r=this;return this.select("visual").fadeOut(100),n=d3.time.format("%Y-%m-%d"),$.ajax("lines.test",{data:{chartId:this.attr.id,product:e.product,tso:e.tso,startTime:n(e.timeRange.from),endTime:n(e.timeRange.to),resolution:e.resolution},success:function(e){return r.select("visual").fadeIn(100),t(null,e)},error:function(e){return t(e)}})},this.onGetLines=function(e,t){var n=this;return this.getLines(t,function(e,t){if(e!=null)throw e;return n.trigger(n.select("visual"),"draw",{data:t}),n.trigger(n.select("lines"),"updateLines",{lines:t})})},this.toggleLine=function(e,t){return this.$node.find(".visual .line"+t.lineId).toggle(200),this.$node.find(".dot"+t.lineId).toggle(200)},this.defaultAttrs({navigation:".navigation",visual:".visual",lines:".lines"}),this.after("initialize",function(){var t,i,s,o,u;return this.on("getLines",this.onGetLines),this.on("updateNavigation",function(e,t){return this.trigger(this.select("visual"),"navigationData",t)}),this.on("toggleLine",this.toggleLine),this.attr.width=(o=this.$node.attr("data-width"))!=null?o:"800",s=$("<div>").addClass("visual"),this.$node.append(s),n.attachTo(s,{id:this.attr.id,type:(u=this.$node.attr("data-chart-type"))!=null?u:"line",width:this.attr.width}),t=$("<div>").addClass("lines").css("width",""+this.attr.width+"px"),this.$node.append(t),r.attachTo(t),i=$("<div>").addClass("navigation"),this.$node.append(i),e.attachTo(i,{id:this.attr.id,width:this.attr.width})})})})}.call(this),function(){require.config({baseUrl:"/enwida/resources/js/chart/lib"}),require(["chart_manager","navigation"],function(e,t){return $(document).ready(function(){return $(".chart").each(function(){var t;return t=$(this).attr("data-chart-id"),e.attachTo($(this),{id:t})})})})}.call(this),define("../charts",function(){});