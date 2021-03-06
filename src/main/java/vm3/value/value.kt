package vm3.value

import vm3.type.Type
import vm3.type.code

sealed class Value {
	data class Argument(val depth: Int) : Value()

	data class F32(val float: Float) : Value()

	data class Struct(val fields: List<Field>) : Value()
	data class Field(val name: String, val value: Value)
	data class StructAt(val lhs: Value, val name: String) : Value()

	data class Switch(val lhs: Value, var functions: List<Function>) : Value()

	data class Call(val function: Function, val param: Value) : Value()

	data class Plus(val lhs: Value, val rhs: Value) : Value()
	data class Minus(val lhs: Value, val rhs: Value) : Value()
	data class Times(val lhs: Value, val rhs: Value) : Value()

	data class Function(val param: Type, val body: Value)
}

val Value.code: String
	get() =
		when (this) {
			is Value.Argument -> if (depth == 0) "argument" else "argument(-$depth)"
			is Value.F32 -> "$float"
			is Value.Struct -> "{${fields.joinToString(", ") { it.code }}}"
			is Value.StructAt -> "${lhs.code}.$name"
			is Value.Switch -> "${lhs.code}.switch { ${functions.joinToString(", ") { it.switchCode }} }"
			is Value.Plus -> "${lhs.code}.plus(${rhs.code})"
			is Value.Minus -> "${lhs.code}.minus(${rhs.code})"
			is Value.Times -> "${lhs.code}.times(${rhs.code})"
			is Value.Call -> "${function.code}.call(${param.code})"
		}

val Value.Function.code: String
	get() =
		"${param.code}.gives(${body.code})"

val Value.Function.switchCode: String
	get() =
		"${param.code} => ${body.code}"

val Value.Field.code: String
	get() =
		"$name: ${value.code}"

val Value.floatOrNull get() = (this as? Value.F32)?.float
