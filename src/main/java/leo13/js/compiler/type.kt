package leo13.js.compiler

import leo.base.fold

sealed class Type
object NumberType : Type()
object StringType : Type()
object NativeType : Type()
data class FieldType(val field: TypeField) : Type()
data class ArrowType(val arrow: Arrow) : Type()

sealed class Types
object EmptyTypes : Types()
data class LinkTypes(val link: TypeLink) : Types()

data class TypeLink(val types: Types, val type: Type)
data class TypeField(val string: String, val types: Types)

infix fun Types.linkTo(line: Type) = TypeLink(this, line)
infix fun String.fieldTo(types: Types) = TypeField(this, types)

val emptyTypes: Types = EmptyTypes
val numberType: Type = NumberType
val stringType: Type = StringType
val nativeType: Type = NativeType
fun type(field: TypeField): Type = FieldType(field)
fun type(arrow: Arrow): Type = ArrowType(arrow)

fun Types.plus(type: Type): Types = LinkTypes(this linkTo type)
fun types(vararg types: Type): Types = emptyTypes.fold(types) { plus(it) }