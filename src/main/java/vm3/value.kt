package vm3

sealed class Value {
	object Input : Value()

	data class Bool(val boolean: Boolean) : Value()
	data class I32(val int: Int) : Value()
	data class F32(val float: Float) : Value()

	data class Struct(val values: List<Field>) : Value() {
		data class Field(val name: String, val value: Value)
	}

	data class Array(val values: List<Value>) : Value()
	data class StructAt(val index: Int) : Value()
	data class ArrayAt(val index: Value) : Value()

	data class I32Inc(val lhs: Value) : Value()
	data class I32Dec(val lhs: Value) : Value()
	data class I32UnaryMinus(val lhs: Value) : Value()

	data class I32Plus(val lhs: Value, val rhs: Value) : Value()
	data class I32Minus(val lhs: Value, val rhs: Value) : Value()
	data class I32Times(val lhs: Value, val rhs: Value) : Value()
	data class I32Div(val lhs: Value, val rhs: Value) : Value()

	data class I32Inv(val lhs: Value) : Value()
	data class I32And(val lhs: Value, val rhs: Value) : Value()
	data class I32Or(val lhs: Value, val rhs: Value) : Value()
	data class I32Xor(val lhs: Value, val rhs: Value) : Value()

	data class I32IsZero(val lhs: Value) : Value()

	data class I32Eq(val lhs: Value, val rhs: Value) : Value()
	data class I32Neq(val lhs: Value, val rhs: Value) : Value()
	data class I32Gt(val lhs: Value, val rhs: Value) : Value()
	data class I32Ge(val lhs: Value, val rhs: Value) : Value()
	data class I32Lt(val lhs: Value, val rhs: Value) : Value()
	data class I32Le(val lhs: Value, val rhs: Value) : Value()

	data class F32Plus(val lhs: Value, val rhs: Value) : Value()
	data class F32Minus(val lhs: Value, val rhs: Value) : Value()
	data class F32Times(val lhs: Value, val rhs: Value) : Value()
	data class F32Div(val lhs: Value, val rhs: Value) : Value()

	data class F32IsZero(val lhs: Value) : Value()

	data class F32Equals(val lhs: Value, val rhs: Value) : Value()
	data class F32NotEquals(val lhs: Value, val rhs: Value) : Value()
	data class F32IsGreaterThan(val lhs: Value, val rhs: Value) : Value()
	data class F32IfGreaterOrEqualTo(val lhs: Value, val rhs: Value) : Value()
	data class F32IsLessThan(val lhs: Value, val rhs: Value) : Value()
	data class F32IsLessOrEqualTo(val lhs: Value, val rhs: Value) : Value()

	data class F32Sin(val lhs: Value) : Value()
	data class F32Cos(val lhs: Value) : Value()
}

fun Value.type(input: Type): Type =
	when (this) {
		Value.Input -> input
		is Value.Bool -> Type.Bool
		is Value.I32 -> Type.I32
		is Value.F32 -> Type.F32
		is Value.Struct -> Type.Struct(values.map { Type.Struct.Field(it.name, it.value.type(input)) })
		is Value.Array -> Type.Array(values[0].type(input), values.size)
		is Value.StructAt -> TODO()
		is Value.ArrayAt -> TODO()
		is Value.I32Inc -> Type.I32
		is Value.I32Dec -> Type.I32
		is Value.I32UnaryMinus -> Type.I32
		is Value.I32Plus -> Type.I32
		is Value.I32Minus -> Type.I32
		is Value.I32Times -> Type.I32
		is Value.I32Div -> Type.I32
		is Value.I32Inv -> Type.I32
		is Value.I32And -> Type.I32
		is Value.I32Or -> Type.I32
		is Value.I32Xor -> Type.I32
		is Value.I32IsZero -> Type.I32
		is Value.I32Eq -> Type.Bool
		is Value.I32Neq -> Type.Bool
		is Value.I32Gt -> Type.Bool
		is Value.I32Ge -> Type.Bool
		is Value.I32Lt -> Type.Bool
		is Value.I32Le -> Type.Bool
		is Value.F32Plus -> Type.F32
		is Value.F32Minus -> Type.F32
		is Value.F32Times -> Type.F32
		is Value.F32Div -> Type.F32
		is Value.F32IsZero -> Type.F32
		is Value.F32Equals -> Type.Bool
		is Value.F32NotEquals -> Type.Bool
		is Value.F32IsGreaterThan -> Type.Bool
		is Value.F32IfGreaterOrEqualTo -> Type.Bool
		is Value.F32IsLessThan -> Type.Bool
		is Value.F32IsLessOrEqualTo -> Type.Bool
		is Value.F32Sin -> Type.F32
		is Value.F32Cos -> Type.F32
	}