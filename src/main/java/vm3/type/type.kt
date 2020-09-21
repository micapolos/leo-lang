package vm3.type

sealed class Type {
	object Bool : Type()
	object I32 : Type()
	object F32 : Type()
	data class Array(val itemType: Type, val itemCount: Int) : Type()
	data class Struct(val fields: List<Field>) : Type()
	data class Choice(val caseTypes: List<Type>) : Type()
	data class Field(val name: String, val valueType: Type)
}

val Type.size: Int
	get() =
		when (this) {
			Type.Bool -> 4
			Type.I32 -> 4
			Type.F32 -> 4
			is Type.Array -> itemType.size.times(itemCount)
			is Type.Struct -> fields.map { it.valueType.size }.sum()
			is Type.Choice -> 4 + (caseTypes.map { it.size }.max() ?: 0)
		}

val Type.code: String
	get() =
		when (this) {
			Type.Bool -> "bool"
			Type.I32 -> "i32"
			Type.F32 -> "f32"
			is Type.Array -> "${itemType.code}[$itemCount]"
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