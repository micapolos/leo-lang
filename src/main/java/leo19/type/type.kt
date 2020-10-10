package leo19.type

import leo.base.ifOrNull
import leo.base.indexed

sealed class Type
data class NativeType(val native: Any) : Type()
data class StructType(val struct: Struct) : Type()
data class ChoiceType(val choice: Choice) : Type()
data class ArrowType(val arrow: Arrow) : Type()

data class Struct(val fieldList: List<Field>)
data class Choice(val fieldList: List<Field>)

data class Field(val name: String, val type: Type)
data class Arrow(val lhs: Type, val rhs: Type)

fun native(any: Any): Type = NativeType(any)
fun struct(vararg fields: Field): Type = StructType(Struct(fields.toList()))
fun choice(vararg fields: Field): Type = ChoiceType(Choice(fields.toList()))

infix fun String.to(type: Type) = Field(this, type)
val Type.structOrNull: Struct? get() = (this as? StructType)?.struct
val Struct.contentOrNull: Type? get() = ifOrNull(fieldList.size == 1) { fieldList[0].type }

fun Type.getOrNull(name: String) =
	structOrNull
		?.contentOrNull
		?.structOrNull
		?.fieldList
		?.findLast { it.name == name }
		?.let { struct(it) }

fun Struct.indexedOrNull(name: String): IndexedValue<Type>? =
	fieldList.indexOfLast { it.name == name }.let { index ->
		if (index == -1) null
		else index indexed fieldList[index].type
	}
