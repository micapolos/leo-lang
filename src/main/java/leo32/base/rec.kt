package leo32.base

data class Rec(val recOrNull: Rec?)

val rec: Rec get() = Rec(null)
val Rec.back: Rec get() = Rec(this)
