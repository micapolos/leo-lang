package leo14.js.compiler

import leo.base.fold

sealed class Type
object EmptyType : Type()
object NativeType : Type()
data class ArrowType(val arrow: Arrow) : Type()
data class LinkType(val link: TypeLink) : Type()

data class TypeLink(val type: Type, val field: TypeField)
data class TypeField(val string: String, val type: Type)

infix fun Type.linkTo(field: TypeField) = TypeLink(this, field)
infix fun String.fieldTo(type: Type) = TypeField(this, type)

val emptyType: Type = EmptyType
val nativeType: Type = NativeType
fun type(arrow: Arrow): Type = ArrowType(arrow)
fun type(link: TypeLink): Type = LinkType(link)

fun Type.plus(field: TypeField): Type = type(this linkTo field)
fun type(vararg fields: TypeField): Type = emptyType.fold(fields) { plus(it) }