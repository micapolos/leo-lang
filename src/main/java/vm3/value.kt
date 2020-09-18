package vm3

sealed class Value {
	object Input : Value()

	data class Bool(val boolean: Boolean) : Value()
	data class I32(val int: Int) : Value()
	data class F32(val float: Float) : Value()

	data class Array(val values: List<Value>) : Value()
	data class Struct(val values: List<Field>) : Value() {
		data class Field(val name: String, val value: Value)
	}

	data class StructAt(val index: Int) : Value()
	data class ArrayAt(val index: Value) : Value()

	data class Inc(val lhs: Value) : Value()
	data class Dec(val lhs: Value) : Value()

	data class Plus(val lhs: Value, val rhs: Value) : Value()
	data class Minus(val lhs: Value, val rhs: Value) : Value()
}

fun Value.type(input: Type): Type =
	when (this) {
		Value.Input -> input
		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(values.map { Type.Struct.Field(it.name, it.value.type(input)) })
		is Value.Array -> Type.Array(values[0].type(input), values.size)
		is Value.ArrayAt -> TODO()
		is Value.StructAt -> TODO()
		is Value.Inc ->
			when (lhs.type(input)) {
				Type.I32 -> Type.I32
				else -> error("${this}.type(${input}).type")
			}
		is Value.Dec ->
			when (lhs.type(input)) {
				Type.I32 -> Type.I32
				else -> error("${this}.type(${input}).type")
			}
		is Value.Plus ->
			when (lhs.type(input)) {
				Type.I32 -> Type.I32
				Type.F32 -> Type.F32
				else -> error("${this}.type(${input}).type")
			}
		is Value.Minus ->
			when (lhs.type(input)) {
				Type.I32 -> Type.I32
				Type.F32 -> Type.F32
				else -> error("${this}.type(${input}).type")
			}
	}