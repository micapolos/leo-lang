package leo14.typed

import leo.base.fold

sealed class Type

object EmptyType : Type()
object TermType : Type()
object NativeType : Type()
data class ArrowType(val arrow: Arrow) : Type()
data class LinkType(val link: Link) : Type()

data class Arrow(val lhs: Type, val rhs: Type)
data class Link(val lhs: Type, val field: Field)
data class Field(val string: String, val rhs: Type)

val emptyType: Type = EmptyType
val nativeType: Type = NativeType
fun type(link: Link): Type = LinkType(link)

fun Type.plus(field: Field) = type(this linkTo field)
fun type(vararg fields: Field) = emptyType.fold(fields) { plus(it) }

infix fun Type.linkTo(field: Field) = Link(this, field)
infix fun String.fieldTo(type: Type) = Field(this, type)

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
		lhs.isConstant && field.isConstant

val Field.isConstant
	get() =
		rhs.isConstant

val Type.headOrNull: Type?
	get() =
		(this as? LinkType)?.link?.field?.rhs

val Type.tailOrNull: Type?
	get() =
		(this as? LinkType)?.link?.lhs
