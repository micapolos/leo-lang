package vm3

sealed class Value {
	object Input : Value()

	data class Bool(val boolean: Boolean) : Value()
	data class I32(val int: Int) : Value()
	data class F32(val float: Float) : Value()

	data class Array(val items: List<Value>) : Value()
	data class ArrayAt(val lhs: Value, val index: Value) : Value()

	data class Struct(val fields: List<Field>) : Value()
	data class Field(val name: String, val value: Value)
	data class StructAt(val lhs: Value, val name: String) : Value()

	data class Inc(val lhs: Value) : Value()
	data class Dec(val lhs: Value) : Value()

	data class Plus(val lhs: Value, val rhs: Value) : Value()
	data class Minus(val lhs: Value, val rhs: Value) : Value()
}
