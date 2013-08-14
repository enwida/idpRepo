define ->

  flight.component ->

    @createElements = ->
      @$node.append($("<a>")
        .addClass("downloadSvg")
        .attr("download", "chart.svg")
        .attr("title", "chart.svg")
        .append("<button>")
          .addClass("btn")
          .text("Download SVG"))

      @$node.append($("<a>")
        .addClass("downloadCsv")
        .attr("href", "download?chartId=#{@attr.id}")
        .append("<button>")
          .addClass("btn")
          .text("Download CSV"))

    @after "initialize", ->
      @createElements()

      @on "downloadLink", =>
        $("svg").attr({ version: '1.1' , xmlns:"http://www.w3.org/2000/svg"})
        html = @$node.closest(".chart").find(".visual").html()
        html = html.replace(/<svg.*?>/, "")
                   .replace("</svg>", "")

        cssUrl = $('link[href$="chart.css"]').attr "href"
        $.ajax cssUrl,
          error: -> alert "Error while preparing download"
          success: (data) =>
            data = $.base64.encode "<svg><style>#{data}</style>#{html}</svg>"
            @$node.find("a.downloadSvg").attr("href", "data:image/svg;base64," + data)

