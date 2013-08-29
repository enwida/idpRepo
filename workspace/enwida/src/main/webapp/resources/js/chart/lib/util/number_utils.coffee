define ->

  symbols:
    en:
      decimalPoint: "."
      separator: ","
    de:
      decimalPoint: ","
      separator: "'"

  getSymbols: (locale) ->
    @symbols[locale] ? @symbols["de"]

  format: (n, decimals = 0, locale = "en") ->
    sym = @getSymbols locale
    n.valueOf().toFixed(decimals).replace(".", sym.decimalPoint)

