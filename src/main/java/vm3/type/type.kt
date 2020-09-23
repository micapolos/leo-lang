package vm3.type

import leo.base.Seq
import leo.base.nodeOrNull
import leo.base.orNullIf
import vm3.dsl.type.struct

sealed class Type {
	object F32 : Type()
	data class Struct(val fields: List<Field>) : Type()
	data class Choice(val caseTypes: List<Type>) : Type()
	data class Field(val name: String, val valueType: Type)
}

val Type.size: Int
	get() =
		when (this) {
			Type.F32 -> 4
			is Type.Struct -> fields.map { it.valueType.size }.sum()
			is Type.Choice -> 4 + (caseTypes.map { it.size }.max() ?: 0)
		}

val Type.code: String
	get() =
		when (this) {
			Type.F32 -> "f32"
			is Type.Struct ->
				if (fields.isEmpty()) "{}"
				else "{ ${fields.joinToString(", ") { it.code }} }"
			is Type.Choice ->
				if (caseTypes.isEmpty()) "<>"
				else "< ${caseTypes.joinToString(" | ") { it.code }} >"
		}

val Type.Field.code: String
	get() =
		"$name: ${valueType.code}"

val Seq<Type>.arrayItemType: Type
	get() =
		nodeOrNull
			.let { nodeOrNull ->
				if (nodeOrNull == null) struct()
				else nodeOrNull.first.orNullIf(nodeOrNull.remaining.all { it == nodeOrNull.first })
			}
			?: error("$this.arrayItemType")