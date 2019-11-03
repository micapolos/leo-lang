package leo14.typed

sealed class Type

object EmptyType : Type()
object ValueType : Type()
object NativeType : Type()
data class ArrowType(val arrow: Arrow) : Type()
data class LinkType(val link: Link) : Type()

data class Arrow(val lhs: Type, val rhs: Type)
data class Link(val lhs: Type, val line: Line)
data class Line(val string: String, val type: Type)

