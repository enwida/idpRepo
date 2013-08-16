define ->

  flight.component ->

    @createElements = ->
      @$node.append($("<button>")
        .addClass("downloadSvg")
        .addClass("btn")
        .text("Download SVG")
        .click =>
          @doPostRequest "svg", svgData: @getSvgDump()
      )

      @$node.append($("<button>")
        .addClass("downloadPng")
        .addClass("btn")
        .text("Download PNG")
        .click =>
          @doPostRequest "png", svgData: @getSvgDump()
      )

    @doPostRequest = (uri, params) ->
      form = $("<form>")
        .attr("action", uri)
        .attr("method", "POST")

      for key of params
        form.append($("<input>")
          .attr("type", "text")
          .attr("name", key)
          .val(params[key]))

      form.appendTo(@$node).submit().remove()

    @getSvgDump = ->
      $("svg").attr({ version: '1.1' , xmlns: "http://www.w3.org/2000/svg"})
      @$node.closest(".chart").find(".visual").html()

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

