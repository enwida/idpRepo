(function(){define("resolution",[],function(){var e,t,n;return n={QUATER_HOURLY:900,HOURLY:3600,DAILY:86400,WEEKLY:604800,MONTHLY:2592e3,YEARLY:31536e3},e={line:{optimalDensity:25,maximumDensity:15},bar:{optimalDensity:30,maximumDensity:25},minmax:{optimalDensity:25,maximumDensity:15},posneg:{optimalDensity:25,maximumDensity:15}},t=["bar"],{getOptimalResolution:function(r,i,s,o,u){var a,f,l,c,h,p,d,v,m,g,y;u==null&&(u=1);if(r==="carpet")return"HOURLY";v=o/e[r].optimalDensity,d=o/e[r].maximumDensity,_.contains(t,r)&&(v/=u,d/=u),h=(i.to-i.from)/1e3,m=_(_(n).keys()).filter(function(e){return _(s).contains(e)}),l=m.map(function(e){return[e,h/n[e]]}),a=l[0];for(p=g=1,y=l.length;1<=y?g<y:g>y;p=1<=y?++g:--g){f=Math.abs(a[1]-v),c=Math.abs(l[p][1]-v);if(a[1]>d){if(l[p][1]<=d||c<f)a=l[p]}else c<f&&l[p][1]<d&&(a=l[p])}return a[0]}}})}).call(this),function(){define("navigation",["resolution"],function(e){return flight.component(function(){return this.productParts=["type","posneg","timeslot"],this.treeParts=["type","timeslot","posneg"],this.dateFormat=d3.time.format("%Y-%m-%d"),this.datePickerFormat="yyyy-mm-dd",this.timeRanges=["Day","Week","Month","Year"],this.viewModes={Day:"days",Week:"days",Month:"months",Year:"years"},this.datePickerFormats={Day:"yyyy-mm-dd",Week:"yyyy-mm-dd",Month:"yyyy-mm",Year:"yyyy"},this.createElements=function(){var e,t,n,r,i,s,o,u,a,f,l,c=this;n=$("<div>").addClass("productSelect").append($("<select>").addClass("tso").change(function(){return c.trigger("updateProducts")})),t=$("<div>").addClass("product").css("display","inline"),f=this.productParts;for(s=0,u=f.length;s<u;s++)e=f[s],t.append($("<select>").addClass(e).change(function(){return c.trigger("updateProducts")}));n.append(t),i=$("<div>").addClass("timeselect"),l=this.timeRanges;for(o=0,a=l.length;o<a;o++)r=l[o],i.append($("<div>").addClass("input-append").addClass("datepicker-generic").addClass("datepicker-"+r).append($("<input>").attr("type","text").addClass("from").attr("readonly",!0)).append($("<span>").addClass("add-on").addClass("calendar").addClass("fromIcon").append($("<i>").addClass("icon-th"))).hide());return i.append($("<div>").addClass("prevPeriode").append($("<i>").addClass("icon-chevron-left")).click(function(){return c.prevPeriode()})),i.append($("<select>").addClass("timerange")).change(function(){return c.refreshDatepicker()}),i.append($("<div>").addClass("nextPeriode").append($("<i>").addClass("icon-chevron-right")).click(function(){return c.nextPeriode()})),this.$node.append(n),this.$node.append(i)},this.getNavigationData=function(e){var t=this;return $.ajax("navigation.test",{data:{chartId:this.attr.id},success:function(n){return t.navigationData=n,e(null,n)},error:function(t){return e(t)}})},this.refresh=function(){var e=this;return this.getNavigationData(function(t,n){var r;if(t!=null)throw t;return r={from:new Date(n.timeRangeMax.from),to:new Date(n.timeRangeMax.to)},e.fillTso(),e.fillProduct(),e.fillResolutions(),e.fillTimeRange(r),e.setDefaults(n.defaults),e.trigger("updateNavigation",{data:n}),e.triggerGetLines()})},this.fillTso=function(){var e,t,n,r;e=this.select("tso"),e.empty(),n=this.navigationData.tsos,r=[];for(t in this.navigationData.tsos)r.push(e.append($("<option>").val(t).text(n[t])));return r},this.fillProduct=function(){return this.setProduct(111)},this.fillResolutions=function(){var e,t,n,r,i,s;e=this.select("resolution"),e.empty(),i=this.navigationData.allResolutions,s=[];for(n=0,r=i.length;n<r;n++)t=i[n],s.push(e.append($("<option>").val(t).text(t)));return s},this.fillTimeRange=function(e){var t,n,r,i,s,o=this;e==null&&(e={}),this.timeRanges.forEach(function(t){var n,r,i;return n=o.getDatepickerElement(t),n.datepicker({format:o.datePickerFormats[t],weekStart:1,calendarWeeks:t==="Week",weekselect:t==="Week",viewMode:o.viewModes[t],minViewMode:o.viewModes[t],startDate:(r=e.from)!=null?r:"1900-01-01",endDate:(i=e.to)!=null?i:new Date}),n.closest(".datepicker-generic").find(".fromIcon").click(function(e){return $(e.target).closest(".datepicker-generic").find(".from").datepicker("show")}),n.on("changeDate",function(e){return o.updateDatepickers(e.date,t)})}),t=this.select("timeRange"),s=this.timeRanges;for(r=0,i=s.length;r<i;r++)n=s[r],t.append($("<option>").val(n).text(n));return this.refreshDatepicker()},this.refreshDatepicker=function(){var e;return e=this.select("timeRange").val(),this.$node.find(".datepicker-generic").hide(),this.$node.find(".datepicker-"+e).show()},this.updateDatepickers=function(e,t){var n,r,i,s,o,u,a,f;a=this.timeRanges,f=[];for(o=0,u=a.length;o<u;o++){s=a[o];if(s===t)continue;n=this.getDatepickerElement(s),r=function(){switch(t){case"Day":return s==="Week"?this.getWeekStart(e):e;case"Week":return i=new Date(n.data("datepicker").date),i.setMonth(e.getMonth()),i.setFullYear(e.getFullYear()),i;case"Month":return i=new Date(n.data("datepicker").date),i.setMonth(e.getMonth()),i.setFullYear(e.getFullYear()),i;case"Year":return i=new Date(n.data("datepicker").date),i.setFullYear(e.getFullYear()),i}}.call(this),f.push(n.datepicker("setDate",r))}return f},this.setDefaults=function(e){return this.select("tso").val(e.tso),this.setProduct(e.product),this.select("resolution").val(e.resolution),this.select("timeRange").val(this.getTimeRange(e.timeRange)),this.refreshDatepicker(),this.forEachDatepicker(function(t){return t.datepicker("setDate",new Date(e.timeRange.from))})},this.getProductTree=function(){var e;return e=parseInt(this.select("tso").val()),_.find(this.navigationData.productTrees,function(t){return t.tso===e})},this.setProduct=function(e){var t,n,r,i,s,o,u,a,f,l,c,h,p,d,v,m,g;e=""+e,u={};for(r=a=0,p=this.productParts.length;0<=p?a<p:a>p;r=0<=p?++a:--a)i=parseInt(e.substring(r,r+1)),u[this.productParts[r]]=i;o=this.getProductTree().root,d=this.treeParts,g=[];for(f=0,c=d.length;f<c;f++){s=d[f],n=this.select("product").find("."+s),n.empty(),v=o.children;for(l=0,h=v.length;l<h;l++)t=v[l],n.append($("<option>").val(t.id).text(t.name));i=u[s],n.val(i),g.push(o=(m=_.find(o.children,function(e){return e.id===i}))!=null?m:o.children[0])}return g},this.getProduct=function(){var e,t,n,r,i,s;n="",s=this.productParts;for(r=0,i=s.length;r<i;r++)t=s[r],e=this.select("product").find("."+t),n+=e.val();return n},this.updateProducts=function(){return this.setProduct(this.getProduct())},this.getDateFrom=function(e){var t;t=new Date(e);switch(this.select("timeRange").val()){case"Week":t=this.getWeekStart(t);break;case"Month":t.setDate(1);break;case"Year":t.setMonth(0),t.setDate(1)}return t},this.getDateTo=function(e){var t;t=new Date(e);switch(this.select("timeRange").val()){case"Day":t.setDate(t.getDate()+1);break;case"Week":t.setDate(t.getDate()+7);break;case"Month":t.setMonth(t.getMonth()+1);break;case"Year":t.setFullYear(t.getFullYear()+1)}return t},this.getWeekStart=function(e){var t;t=new Date(e);while(t.getDay()!==1)t.setDate(t.getDate()-1);return t},this.getDatepickerElement=function(e){return this.$node.find(".datepicker-"+e+" .from")},this.getVisibleFromDate=function(){return new Date(this.$node.find(".datepicker-generic:visible .from").data("datepicker").date)},this.setVisibleFromDate=function(e){return this.$node.find(".datepicker-generic:visible .from").datepicker("setDate",e)},this.getTimeRange=function(e){var t;return t=e.to-e.from,t<6048e5?"Day":t<24192e5?"Week":t<31536e6?"Month":"Year"},this.forEachDatepicker=function(e){var t,n,r,i,s;i=this.timeRanges,s=[];for(n=0,r=i.length;n<r;n++)t=i[n],s.push(e(this.getDatepickerElement(t)));return s},this.prevPeriode=function(){var e;e=new Date(this.getVisibleFromDate());switch(this.select("timeRange").val()){case"Day":e.setDate(e.getDate()-1);break;case"Week":e.setDate(e.getDate()-7);break;case"Month":e.setMonth(e.getMonth()-1);break;case"Year":e.setFullYear(e.getFullYear()-1)}return this.setVisibleFromDate(e),this.triggerGetLines()},this.nextPeriode=function(){return this.setVisibleFromDate(this.getDateTo(this.getVisibleFromDate())),this.triggerGetLines()},this.triggerGetLines=function(t){var n,r,i,s;return t==null&&(t={}),n=this.getDateFrom((s=t.from)!=null?s:this.getVisibleFromDate()),i={from:n,to:this.getDateTo(n)},r=e.getOptimalResolution(this.attr.type,i,this.navigationData.allResolutions,this.attr.width),this.trigger("getLines",{tso:this.select("tso").val(),product:this.getProduct(),timeRange:i,resolution:r})},this.setupEvents=function(){var e=this;return this.$node.on("change",function(t){return e.triggerGetLines()})},this.defaultAttrs({tso:".tso",product:".product",resolution:".resolution",timeRange:".timerange",type:"line"}),this.after("initialize",function(){return this.on("refresh",this.refresh),this.on("updateProducts",this.updateProducts),this.createElements(),this.refresh(),this.setupEvents()})})})}.call(this),function(){define("spreadsheet",[],function(){var e;return e=function(){function e(e,t){this.isDateScale=t!=null?t:!1,this.element=$(e)}return e.prototype.draw=function(e){var t;return this.element.empty(),t=$("<table>"),t.append(this.getTableHeader(e)),this.element.append(t),this.addTableRows(t,e)},e.prototype.getTableHeader=function(e,t){var n,r,i,s,o;t==null&&(t="Time"),r=$("<tr>"),r.append($("<th>").text(t)),o=[];for(i=0,s=e.length;i<s;i++)n=e[i],console.log(n),o.push(r.append($("<th>").text(n.title)));return o},e.prototype.addTableRows=function(e,t){var n,r,i,s,o,u,a,f,l,c,h,p,d,v,m,g,y,b,w,E,S,x;console.log("datescale: "+this.isDateScale),i=d3.time.format("%Y-%m-%d %H:%M"),a={};for(l=0,d=t.length;l<d;l++){o=t[l],b=o.dataPoints;for(c=0,v=b.length;c<v;c++)s=b[c],(w=a[y=s.x])==null&&(a[y]={}),a[s.x][o.title]=s.y}E=_(a).keys().sort(),x=[];for(h=0,m=E.length;h<m;h++){f=E[h],u=$("<tr>"),n=$("<td>"),n.text(this.isDateScale?i(new Date(parseInt(f))):f),u.append(n);for(p=0,g=t.length;p<g;p++)o=t[p],r=$("<td>"),r.text((S=a[f][o.title])!=null?S:""),u.append(r);x.push(e.append(u))}return x},e}()})}.call(this),function(){define("scale",[],function(){var e=this;return{init:function(e){var t,n;return this.chart=e,this.options=this.chart.options.scale,this.setupXScale(),this.setupYScale(),this.setupDomain(this.chart.xScale,"x"),this.setupDomain(this.chart.yScale,"y"),typeof (t=this.chart.xScale).nice=="function"&&t.nice(),typeof (n=this.chart.yScale).nice=="function"?n.nice():void 0},setupXScale:function(){if(!(this.options&&this.options.x&&this.options.x.type)){this.setupXScaleLinear();return}switch(this.options.x.type){case"linear":return this.setupXScaleLinear();case"ordinal":return this.setupXScaleOrdinal();case"date":return this.setupXScaleDate()}},setupYScale:function(){var e,t;if(!((e=this.options)!=null?(t=e.y)!=null?t.type:void 0:void 0)){this.setupYScaleLinear();return}switch(this.options.y.type){case"ordinal":return this.setupYScaleOrdinal();default:return this.setupYScaleLinear()}},setupXScaleLinear:function(){return this.chart.xScale=d3.scale.linear().range([0,this.chart.options.width])},setupYScaleOrdinal:function(){var e,t,n,r;return(n=(t=this.options["y"])["domain"])==null&&(t.domain={type:"map"}),e=(r=this.options.y.padding)!=null?r:.1,this.chart.yScale=d3.scale.ordinal().rangeRoundBands([0,this.chart.options.height],e)},setupXScaleOrdinal:function(){var e,t,n,r;return(n=(t=this.options["x"])["domain"])==null&&(t.domain={type:"map"}),e=(r=this.options.x.padding)!=null?r:.1,this.chart.xScale=d3.scale.ordinal().rangeRoundBands([0,this.chart.options.width],e)},setupXScaleDate:function(){return this.chart.xScale=d3.time.scale().range([0,this.chart.options.width]),this.chart.xScale.tickFormat=function(e){return function(e){return e.getTime()}}},getTickFormater:function(e){return function(t){var n;return n=null,function(t){var r,i,s,o,u;s=function(e){return n=t,d3.time.format(e[1])(t)};if(n==null)return s(e[0]);for(o=0,u=e.length;o<u;o++){i=e[o],r=d3.time.format(i[0]);if(r(t)!==r(n))return s(i)}return s(e[e.length-1])}}},setupYScaleLinear:function(){return this.chart.yScale=d3.scale.linear().range([this.chart.options.height,0])},setupDomain:function(e,t){var n,r,i,s,o;if(this.options==null||this.options[t]==null||this.options[t]["domain"]==null||!this.options[t].domain){this.setupDomainExtent(e,t);return}r=this.options[t].domain.type;if(r instanceof Array){o=[];for(i=0,s=r.length;i<s;i++)n=r[i],o.push(this.applyStrategy(n,e,t));return o}return this.applyStrategy(r,e,t)},applyStrategy:function(e,t,n){switch(e){case"extent":return this.setupDomainExtent(t,n);case"map":return this.setupDomainMap(t,n);case"stretch":return this.setupDomainStretch(t);case"fixed":return this.setupDomainFixed(t,n);default:throw new Error("Not a valid scale type: "+e)}},setupDomainExtent:function(e,t){return e.domain(d3.extent(this.allValues(t)))},setupDomainStretch:function(e,t,n){var r;return t==null&&(t=.9),n==null&&(n=1.1),r=e.domain(),e.domain([r[0]*t,r[1]*n])},setupDomainMap:function(e,t){return e.domain(this.allValues(t))},setupDomainFixed:function(e,t){var n,r;return r=this.getBound(this.options[t].domain.low,t),n=this.getBound(this.options[t].domain.high,t),e.domain([r,n])},allValues:function(e){var t,n,r,i,s,o,u,a,f,l;t=[],s={},l=this.chart.data;for(r=o=0,a=l.length;o<a;r=++o){i=l[r];if(_(this.chart.options.disabledLines).contains(r))continue;for(u=0,f=i.length;u<f;u++)n=i[u],s[n[e]]==null&&(t.push(n[e]),s[n[e]]=!0)}return t},getBound:function(e,t){if($.isNumeric(e))return e;switch(e){case"min":return d3.min(this.chart.data,function(e){return e[t]});case"max":return d3.max(this.chart.data,function(e){return e[t]});default:throw new Error("Not a valid bound: "+e)}},getMinInterval:function(e,t){var n,r,i,s,o;t==null&&(t="x"),i=Infinity;for(r=s=0,o=e.length-2;0<=o?s<o:s>o;r=0<=o?++s:--s)n=Math.abs(e[r+1][t]-e[r][t]),n>0&&n<i&&(i=n);return i},getBarWidth:function(e,t,n){var r,i;return n==null&&(n="x"),r=e[0][n],i=this.getMinInterval(e,n),t(r+i)-t(r)}}})}.call(this),function(){define("generic_chart",["scale"],function(e){var t;return t=function(){function t(t){var n,r,i;if(t==null||t.lines==null)throw"No data given";n={margin:{top:20,left:50,right:20,bottom:30},width:960,height:500,parent:"body",disabledLines:[]},this.options=$.extend(n,t),this.lines=t.lines,this.xLabel=(r=this.options.xLabel)!=null?r:"",this.yLabel=(i=this.options.yLabel)!=null?i:"",this.lineLabels=this.lines.map(function(e){return e.title}),this.data=this.lines.map(function(e){return e.dataPoints}),e.init(this),this.generateAxes()}return t.prototype.generateXAxis=function(){return this.xAxis=d3.svg.axis().scale(this.xScale).orient("bottom")},t.prototype.generateYAxis=function(){return this.yAxis=d3.svg.axis().scale(this.yScale).orient("left")},t.prototype.generateAxes=function(e,t){return this.generateXAxis(),this.generateYAxis()},t.prototype.drawSvg=function(){var e,t;if(this.svg!=null)return;return t=this.options.width+this.options.margin.left+this.options.margin.right,e=this.options.height+this.options.margin.top+this.options.margin.bottom,this.svg=d3.selectAll(this.options.parent).append("svg").attr("width",t).attr("height",e).append("g").attr("transform","translate("+this.options.margin.left+","+this.options.margin.top+")")},t.prototype.drawXAxis=function(e,t,n){t==null&&(t=0),n==null&&(n=0),this.svg.append("g").attr("class","x axis").attr("transform","translate("+t+","+(n+this.options.height)+")").call(e).append("text").attr("x",this.options.width).attr("y",-5).attr("text-anchor","end").text(""+this.xLabel);if(this.options.scale.x.type==="date")return this.makeDateScale()},t.prototype.drawYAxis=function(e){return this.svg.append("g").attr("class","y axis").call(e).append("text").attr("transform","rotate(-90)").attr("y",12).attr("text-anchor","end").text(""+this.yLabel)},t.prototype.drawLegend=function(){return},t.prototype.drawAxes=function(e,t){return e==null&&(e=0),t==null&&(t=0),this.drawXAxis(this.xAxis,e,t),this.drawYAxis(this.yAxis)},t.prototype.makeDateScale=function(){var t,n,r,i,s,o,u,a,f,l,c;n=(u=(a=this.options)!=null?(f=a.x)!=null?f.dateFormats:void 0:void 0)!=null?u:[["%Y","%Y-%m-%d"],["%m","%b"],["%d","%d"],["%H:%M","%H:%M"]],i=e.getTickFormater(n)(),l=$(this.options.parent).find("g.tick text"),c=[];for(s=0,o=l.length;s<o;s++)r=l[s],t=new Date(parseInt($(r).text().replace(/,/g,""))),$(r).text(i(t)),$(r).attr("original-title",d3.time.format("%Y-%m-%d %H:%M")(t)),c.push($(r).tipsy());return c},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("line_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t)}return t.prototype.generateLine=function(){var e=this;return d3.svg.line().x(function(t){return e.chart.xScale(t.x)}).y(function(t){return e.chart.yScale(t.y)})},t.prototype.drawLine=function(e,t){var n;return t==null&&(t=0),n=this.generateLine(),this.chart.svg.append("path").datum(e).attr("class","line line"+t+" visual"+t).attr("data-legend",this.chart.lineLabels[t]).attr("d",n)},t.prototype.drawDots=function(e,t){var n=this;return t==null&&(t=0),this.chart.svg.selectAll("dot").data(e).enter().append("circle").attr("class","dot dot"+t+" visual"+t).attr("r",4.5).attr("cx",function(e){return n.chart.xScale(e.x)}).attr("cy",function(e){return n.chart.yScale(e.y)}).attr("original-title",function(e){var r,i,s,o;return r=e.x,((i=n.chart.options)!=null?(s=i.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(r=d3.time.format("%Y-%m-%d %H:%M")(new Date(r))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(n.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(n.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(r)))).append($("<tr>").append($("<td align='left'>").text(n.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},t.prototype.draw=function(){var e,t,n,r,i;this.chart.drawSvg(),this.chart.drawAxes(),i=this.chart.data;for(e=n=0,r=i.length;n<r;e=++n)t=i[e],this.drawLine(t,e),this.drawDots(t,e);return this.chart.drawLegend()},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("bar_chart",["generic_chart","scale"],function(e,t){var n;return n=function(){function t(t){var n;t=$.extend(!0,{},t),n=t.scale.x.type,t.scale.x.type="ordinal",this.chart=e.init(t),t.scale.x.type=n}return t.prototype.drawBars=function(e,t){var n,r,i=this;return t==null&&(t=0),r=this.chart.xScale.rangeBand()/e.length,n=0,this.chart.svg.selectAll(".foo").data(e[t]).enter().append("rect").attr("class","bar bar"+t+" visual"+t+" line"+t+" line").attr("x",function(e){return i.chart.xScale(e.x)-n+t*r}).attr("y",function(e){return i.chart.yScale(e.y)}).attr("width",r).attr("height",function(e){return i.chart.options.height-i.chart.yScale(e.y)}).attr("original-title",function(e){var n,r,s,o;return n=e.x,((r=i.chart.options)!=null?(s=r.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(n=d3.time.format("%Y-%m-%d %H:%M")(new Date(n))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(i.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(i.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(n)))).append($("<tr>").append($("<td align='left'>").text(i.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},t.prototype.minInterval=function(e,t){var n,r,i,s,o;t==null&&(t=0),o=null,s=this.chart.data[t][0][e],i=1;while(i<this.chart.data[t].length){n=this.chart.data[t][i][e],r=n-s;if(!o||o>r)o=r;s=n,i+=1}return o},t.prototype.draw=function(){var e,t,n,r,i,s;this.chart.drawSvg(),this.chart.drawAxes(),i=this.chart.data,s=[];for(e=n=0,r=i.length;n<r;e=++n)t=i[e],s.push(this.drawBars(this.chart.data,e));return s},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new n(e)}}})}.call(this),function(){define("carpet_chart",["generic_chart","scale"],function(e,t){var n;return n=function(){function n(n){var r;this.chart=e.init(n),this.chart.options.scale.x.type="date",r=_(this.chart.data[0].map(function(e){return e.y})).uniq().sort(function(e,t){return e-t}),r.push(r[r.length-1]+1),this.chart.yScale.domain([r[0],r[r.length-1]]),this.rectWidth=t.getBarWidth(this.chart.data[0],this.chart.xScale,"x"),this.rectHeight=this.chart.options.height/(r.length-1)}return n.prototype.drawCarpet=function(e,t){var n=this;return t==null&&(t=0),this.color=d3.scale.linear().range(["#00f","#f00"]).domain(d3.extent(e.map(function(e){return e.v}))),this.chart.svg.selectAll(".carpet").data(e).enter().append("rect").attr("class","carpet").attr("x",function(e){return n.chart.xScale(e.x)}).attr("y",function(e){return n.chart.yScale(e.y)-n.rectHeight}).attr("fill",function(e){return n.color(e.v)}).attr("width",this.rectWidth).attr("height",this.rectHeight).attr("original-title",function(e){var r,i;return r=new Date(e.x),r.setHours(e.y),i=d3.time.format("%Y-%m-%d %H:%M")(r),$("<div>").append($("<h6>").addClass("tooltip"+t).text(n.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(n.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(i)))).append($("<tr>").append($("<td align='left'>").text(n.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.v))))).html()})},n.prototype.drawLegend=function(){var e,t,n,r,i,s,o,u,a;r=this.chart.options.margin,s=d3.selectAll(this.chart.options.parent).append("svg").attr("class","legend").attr("width",150).attr("height",this.chart.options.height+r.bottom+r.top+30).append("g").attr("transform","translate(0,"+(r.top+30)+")"),t=this.color.domain()[0],i=(this.color.domain()[1]-t)/10,e=this.chart.options.height/10;for(n=u=0;u<10;n=++u)s.append("rect").attr("x",0).attr("y",n*e).attr("width",20).attr("height",e).attr("fill",this.color(t+(10-n+1)*i));for(n=a=0;a<=10;n=++a)o=n*e,n===0&&(o+=1),n===10&&(o-=1),s.append("line").attr("x1",20).attr("x2",26).attr("y1",o).attr("y2",o).attr("fill","#000000").attr("stroke","#000000"),s.append("text").attr("x",30).attr("y",n*e+3).text(Math.round((t+(10-n)*i)*1e3)/1e3);return s.append("text").attr("x",0).attr("y",-25).text(this.chart.options.yLabel).attr("font-weight","bold")},n.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.drawCarpet(this.chart.data[0]),this.drawLegend()},n}(),{init:function(e){return new n(e)}}})}.call(this),function(){define("min_max_chart",["generic_chart"],function(e){var t;return t=function(){function t(t){this.chart=e.init(t),this.chart.yScale.domain(d3.extent(this.getAllYs())),this.barWidth=this.calculateBarWidth(),this.barOffset=this.barWidth/2,this.chart.xScale.range([this.barOffset,this.chart.options.width-this.barOffset]),this.barWidth=this.calculateBarWidth(),this.barWidth*=.8,this.barOffset=this.barWidth/2}return t.prototype.calculateBarWidth=function(){var e,t,n;return e=this.chart.data[1],n=this.chart.xScale(e[0].x),t=this.chart.xScale(e[0].x+this.getSmallestDistance(e,"x")),t-n},t.prototype.generateLine=function(){var e=this;return d3.svg.line().x(function(t){return e.chart.xScale(t.x)}).y(function(t){return e.chart.yScale(t.y)})},t.prototype.drawLine=function(e,t){var n;return t==null&&(t=0),n=this.generateLine(),this.chart.svg.append("path").datum(e).attr("class","line line"+t+" visual"+t).attr("data-legend",this.chart.lineLabels[t]).attr("d",n)},t.prototype.drawDots=function(e,t){var n=this;return t==null&&(t=0),this.chart.svg.selectAll("dot").data(e).enter().append("circle").attr("class","dot dot"+t+" visual"+t).attr("r",4.5).attr("cx",function(e){return n.chart.xScale(e.x)}).attr("cy",function(e){return n.chart.yScale(e.y)}).attr("original-title",function(e){var r,i,s,o;return r=e.x,((i=n.chart.options)!=null?(s=i.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(r=d3.time.format("%Y-%m-%d %H:%M")(new Date(r))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(n.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(n.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(r)))).append($("<tr>").append($("<td align='left'>").text(n.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},t.prototype.drawBars=function(e,t){var n=this;return t==null&&(t=0),this.chart.svg.selectAll(".foo").data(e).enter().append("rect").attr("class","bar bar"+t+" visual"+t+" line"+t+" line").attr("x",function(e){return n.chart.xScale(e.x)-n.barOffset}).attr("y",function(e){return n.chart.yScale(e.max)}).attr("width",this.barWidth).attr("height",function(e){return n.chart.yScale(e.min)-n.chart.yScale(e.max)}).attr("original-title",function(e){var r,i,s,o;return r=e.x,((i=n.chart.options)!=null?(s=i.scale)!=null?(o=s.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(r=d3.time.format("%Y-%m-%d %H:%M")(new Date(r))),$("<div>").append($("<h6>").addClass("minmax").addClass("tooltip"+t).text(n.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(n.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(r)))).append($("<tr>").append($("<td align='left'>").text(n.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(""+e.min+" - "+e.max))))).html()})},t.prototype.getAllYs=function(){var e;return e=[],_(this.chart.options.disabledLines).contains(0)||(e=e.concat(this.chart.data[0].map(function(e){return e.y}))),_(this.chart.options.disabledLines).contains(1)||(e=e.concat(this.chart.data[1].map(function(e){return e.min})),e=e.concat(this.chart.data[1].map(function(e){return e.max}))),e},t.prototype.getSmallestDistance=function(e,t){var n,r,i,s,o;t==null&&(t="x"),i=Infinity;for(r=s=0,o=e.length-2;0<=o?s<o:s>o;r=0<=o?++s:--s)n=Math.abs(e[r+1][t]-e[r][t]),n<i&&(i=n);return i},t.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.chart.svg.select(".x.axis path").attr("d","M0,6V0H"+this.chart.options.width+"V6"),this.drawBars(this.chart.data[1],1),this.drawLine(this.chart.data[0],0),this.drawDots(this.chart.data[0],0),this.chart.drawLegend()},t.prototype.redraw=function(){return this.chart.svg=null,this.draw()},t}(),{init:function(e){return new t(e)}}})}.call(this),function(){define("pos_neg_chart",["generic_chart","scale"],function(e,t){var n;return n=function(){function n(n){this.chart=e.init(n),this.barWidth=t.getBarWidth(this.chart.data[1],this.chart.xScale,"x"),this.barOffset=this.barWidth/2,this.chart.xScale.range([this.barOffset,this.chart.options.width-this.barOffset]),this.barWidth=t.getBarWidth(this.chart.data[1],this.chart.xScale,"x"),this.barWidth*=.8,this.barOffset=this.barWidth/2}return n.prototype.drawBars=function(e,t,n){var r,i,s=this;return t==null&&(t=0),n==null&&(n=!0),i=function(e){return n?s.chart.yScale(e.y):s.chart.yScale(0)},r=function(e){return n?s.chart.yScale(0)-s.chart.yScale(e.y):s.chart.yScale(e.y)-s.chart.yScale(0)},this.chart.svg.selectAll(".foo").data(e).enter().append("rect").attr("class","bar bar"+t+" visual"+t+" line"+t+" line").attr("x",function(e){return s.chart.xScale(e.x)-s.barOffset}).attr("y",i).attr("width",this.barWidth).attr("height",r).attr("original-title",function(e){var n,r,i,o;return n=e.x,((r=s.chart.options)!=null?(i=r.scale)!=null?(o=i.x)!=null?o.type:void 0:void 0:void 0)==="date"&&(n=d3.time.format("%Y-%m-%d %H:%M")(new Date(n))),$("<div>").append($("<h6>").addClass("tooltip"+t).text(s.chart.lines[t].title)).append($("<table cellpadding='2'>").append($("<tr>").append($("<td align='left'>").text(s.chart.xLabel)).append($("<td align='left'>").append($("<b>").text(n)))).append($("<tr>").append($("<td align='left'>").text(s.chart.yLabel)).append($("<td align='left'>").append($("<b>").text(e.y))))).html()})},n.prototype.draw=function(){return this.chart.drawSvg(),this.chart.drawAxes(),this.chart.svg.select(".x.axis path").attr("d","M0,6V0H"+this.chart.options.width+"V6"),this.drawBars(this.chart.data[0],0,!0),this.drawBars(this.chart.data[1],1,!1),this.chart.drawLegend()},n}(),{init:function(e){return new n(e)}}})}.call(this),function(){define("visual",["line_chart","bar_chart","carpet_chart","min_max_chart","pos_neg_chart"],function(e,t,n,r,i){return flight.component(function(){return this.onNavigationData=function(e,t){return this.attr.chartOptions.xLabel=t.data.xAxisLabel,this.attr.chartOptions.yLabel=t.data.yAxisLabel,this.attr.chartOptions.scale={x:{type:t.data.isDateScale?"date":"linear"}}},this.getChart=function(s){this.attr.chartOptions.lines=s;switch(this.attr.type){case"line":return e.init(this.attr.chartOptions);case"bar":return t.init(this.attr.chartOptions);case"minmax":return r.init(this.attr.chartOptions);case"carpet":return n.init(this.attr.chartOptions);case"posneg":return i.init(this.attr.chartOptions);default:return console.log("Unknown chart type: '"+this.attr.type+"'")}},this.hideDisabledLines=function(e){var t,n,r,i;i=[];for(n=0,r=e.length;n<r;n++)t=e[n],this.$node.find(".line"+t).hide(),i.push(this.$node.find(".dot"+t).hide());return i},this.draw=function(e,t){var n;this.$node.empty(),this.attr.chartOptions.disabledLines=t.disabledLines,n=this.getChart(t.data),n.draw(),this.hideDisabledLines(t.disabledLines);switch(this.attr.type){case"line":return this.$node.find("circle").tipsy({gravity:"sw",html:!0,opacity:.95});case"bar":return this.$node.find("rect").tipsy({gravity:"sw",html:!0,opacity:.95});case"minmax":return this.$node.find("circle").tipsy({gravity:"sw",html:!0,opacity:.95}),this.$node.find("rect").tipsy({gravity:"sw",html:!0,opacity:.95});case"carpet":return this.$node.find("rect").tipsy({gravity:"sw",html:!0,opacity:.95});case"posneg":return this.$node.find("rect").tipsy({gravity:"sw",html:!0,opacity:.95})}},this.defaultAttrs({ChartClass:e}),this.after("initialize",function(){return this.on("navigationData",this.onNavigationData),this.on("draw",this.draw),this.attr.chartOptions={parent:this.$node,width:this.attr.width}})})})}.call(this),function(){define("lines",[],function(){return flight.component(function(){var e,t;return e=function(e,t){var n;return n=e.attr("class"),n+=" "+t,e.attr("class",n)},t=function(e,t){var n;return n=e.attr("class"),n=n.replace(new RegExp("s*"+t+"s*","g"),""),e.attr("class",n)},this.updateLines=function(n,r){var i,s,o,u,a,f,l,c,h,p=this;this.$node.empty(),s=$("<ul>"),this.$node.append(s),function(){c=[];for(var e=0,t=r.lines.length;0<=t?e<t:e>t;0<=t?e++:e--)c.push(e);return c}.apply(this).forEach(function(i){var o,u;return u=r.lines[i],o=$("<li>").addClass("line"+i).append($("<table>").attr("cellpadding","3").append($("<tr>").append($("<td>").append($("<div>").addClass("linesquare"))).append($("<td>").text(u.title)))),o.click(function(){return n=window._,n.contains(p.attr.disabledLines,i)?p.attr.disabledLines=n.without(p.attr.disabledLines,i):p.attr.disabledLines.push(i),o.toggleClass("hidden"),p.trigger("toggleLine",{disabledLines:p.attr.disabledLines})}),o.hover(function(){return p.$node.closest(".chart").find(".line"+i).each(function(){return e($(this),"selected"),$(this).show()}),p.$node.closest(".chart").find(".dot"+i).each(function(){return e($(this),"selected"),$(this).show()})},function(){p.$node.closest(".chart").find(".line"+i).each(function(){return t($(this),"selected")}),p.$node.closest(".chart").find(".dot"+i).each(function(){return t($(this),"selected")});if(o.hasClass("hidden"))return p.$node.closest(".chart").find(".visual .line"+i).hide(),p.$node.closest(".chart").find(".visual .dot"+i).hide()}),s.append(o)}),this.trigger("toggleLine",{disabledLines:this.attr.disabledLines}),l=this.attr.disabledLines,h=[];for(u=0,a=l.length;u<a;u++)i=l[u],h.push(this.$node.find("li.line"+i).addClass("hidden"));return h},this.after("initialize",function(){return this.attr.disabledLines=[],this.on("updateLines",this.updateLines)})})})}.call(this),function(){define("loading",[],function(){return{showText:function(e,t,n,r){var i;return n==null&&(n=960),r==null&&(r=600),e=d3.selectAll(e),i=e.select("svg"),i.empty()?i=e.append("svg").attr("width",n).attr("height",r):(n=parseInt(i.attr("width")),r=parseInt(i.attr("height"))),i.selectAll("*").remove(),i.append("g").attr("transform","translate("+n/2+","+r/2+")").append("text").attr("font-size","24").style("text-anchor","middle").text(t)},showLoading:function(e,t,n){return this.showText(e,"Loading...",t,n)},of:function(e,t,n){var r=this;return{showText:function(i){return r.showText(e,i,t,n)},showLoading:function(){return r.showLoading(e,t,n)}}}}})}.call(this),function(){define("chart_manager",["navigation","spreadsheet","visual","lines","loading"],function(e,t,n,r,i){return flight.component(function(){return this.getMsg=function(){var e,t;return i.of(this.select("visual"),(e=this.attr.navigationData)!=null?e.width:void 0,(t=this.attr.navigationData)!=null?t.height:void 0)},this.getLines=function(e,t){var n,r=this;return this.getMsg().showLoading(),n=d3.time.format("%Y-%m-%d"),$.ajax("lines.test",{data:{chartId:this.attr.id,product:e.product,tso:e.tso,startTime:n(e.timeRange.from),endTime:n(e.timeRange.to),resolution:e.resolution},success:function(e){return t(null,e)},error:function(e){return t(e)}})},this.onGetLines=function(e,t){var n=this;return this.getLines(t,function(e,t){if(e!=null)throw e;return t.length===0?n.getMsg().showText("No data"):(n.attr.data=t=n.preprocessLines(t),n.triggerDraw(t),n.trigger(n.select("lines"),"updateLines",{lines:t}))})},this.triggerDraw=function(e){return e.length===this.attr.disabledLines.length?this.getMsg().showText("No lines selected"):this.trigger(this.select("visual"),"draw",{data:e,disabledLines:this.attr.disabledLines})},this.preprocessLines=function(e){var t,n,r,i,s,o,u,a,f,l,c,h,p,d,v,m;switch(this.attr.type){case"minmax":u=[],u.push(e[1]),o=e[0],o.title+=" / "+e[2].title,d=o.dataPoints;for(i=a=0,c=d.length;a<c;i=++a)n=d[i],n.min=n.y,n.max=e[2].dataPoints[i].y;u.push(o),e=u;break;case"carpet":s=e[0],r=d3.time.format("%H"),v=s.dataPoints;for(f=0,h=v.length;f<h;f++)n=v[f],t=new Date(n.x),n.v=n.y,n.y=parseInt(r(t)),t.setHours(0),t.setMinutes(0),t.setSeconds(0),t.setMilliseconds(0),n.x=t.getTime();e=[s];break;case"posneg":m=e[1].dataPoints;for(l=0,p=m.length;l<p;l++)n=m[l],n.y*=-1;e=[e[0],e[1]]}return e},this.toggleLine=function(e,t){return this.attr.disabledLines=t.disabledLines,this.triggerDraw(this.attr.data)},this.defaultAttrs({navigation:".navigation",visual:".visual",lines:".lines",disabledLines:[]}),this.after("initialize",function(){var t,i,s,o,u;return this.on("getLines",this.onGetLines),this.on("updateNavigation",function(e,t){return this.attr.navigationData=t.data,this.trigger(this.select("visual"),"navigationData",t)}),this.on("toggleLine",this.toggleLine),this.attr.type=(o=this.$node.attr("data-chart-type"))!=null?o:"line",this.attr.width=(u=parseInt(this.$node.attr("data-width")))!=null?u:800,s=$("<div>").addClass("visual"),this.$node.append(s),n.attachTo(s,{id:this.attr.id,type:this.attr.type,width:this.attr.width}),this.attr.type!=="carpet"&&(t=$("<div>").addClass("lines").css("width",""+this.attr.width+"px"),this.$node.append(t),r.attachTo(t)),i=$("<div>").addClass("navigation"),this.$node.append(i),e.attachTo(i,{id:this.attr.id,width:this.attr.width,type:this.attr.type})})})})}.call(this),function(){require.config({baseUrl:"/enwida/resources/js/chart/lib"}),require(["chart_manager","navigation"],function(e,t){return $(document).ready(function(){return $(".chart").each(function(){var t;return t=$(this).attr("data-chart-id"),e.attachTo($(this),{id:t})})})})}.call(this),define("../charts",function(){});