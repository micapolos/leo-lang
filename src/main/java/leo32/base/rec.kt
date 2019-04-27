package leo32.base

sealed class Rec
data class SelfRec(val self: Self) : Rec()
data class BackRec(val rec: Rec) : Rec()

val Self.rec get() = SelfRec(this) as Rec
val Rec.back get() = BackRec(this) as Rec
