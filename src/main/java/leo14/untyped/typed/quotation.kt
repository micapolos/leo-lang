package leo14.untyped.typed

inline class Quotation(val int: Int)

val Int.quoteDepth get() = Quotation(this)
val Quotation.increase get() = int.inc().quoteDepth
val Quotation.decrease get() = int.dec().quoteDepth
val Quotation.isQuoted get() = int > 0
