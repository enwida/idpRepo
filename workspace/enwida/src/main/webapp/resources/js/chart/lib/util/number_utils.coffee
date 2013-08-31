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
    n = n.valueOf().toFixed(decimals).replace(".", sym.decimalPoint)
    @addSeparators n, sym

  addSeparators: (n, sym) ->
    slots = @separatorSlots n
    n = "" + n # Convert to string
    return n if slots.length < 1
    parts = [n.substr(0, slots[0]), n.substr(slots[0])]

    for i in [1...slots.length]
      parts.push parts[i].substr(slots[i] - slots[i - 1])
      parts[i] = parts[i].substr(0, slots[i] - slots[i - 1])

    parts.join sym.separator

  separatorSlots: (n) ->
    n = "" + parseInt(n) # Strip decimals and convert to string
    neg = n.substr(0, 1) is "-"
    n = n.replace("-", "") if neg

    count = parseInt (n.length - 1) / 3
    first = n.length % 3
    first = 3 if first is 0
    [0...count].map (i) -> i * 3 + first + if neg then 1 else 0

