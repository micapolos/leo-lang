package leo14.typed

sealed class Type

object EmptyType : Type()
object TermType : Type()
object NativeType : Type()
data class ArrowType(val arrow: Arrow) : Type()
data class LinkType(val link: Link) : Type()

data class Arrow(val lhs: Type, val rhs: Type)
data class Link(val lhs: Type, val line: Line)
data class Line(val string: String, val type: Type)

val emptyType: Type = EmptyType
val nativeType: Type = NativeType

val Type.isConstant: Boolean
	get() =
		when (this) {
			is EmptyType -> true
			is TermType -> false // TODO: Can it be optimized? Lambda expressions without free variables can be made empty.
			is NativeType -> false // TODO: We should parametrize type by native type, as ask native if it's empty
			is ArrowType -> arrow.isConstant
			is LinkType -> link.isConstant
		}

val Arrow.isConstant
	get() =
		rhs.isConstant // TODO: We may need to check free variables

val Link.isConstant
	get() =
		lhs.isConstant && line.isConstant

val Line.isConstant
	get() =
		type.isConstant
