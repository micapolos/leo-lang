package vm3

import vm3.value.Value

val Value.optimize: Value
	get() =
		innerOptimize.outerOptimize

val Value.outerOptimize: Value
	get() =
		when (this) {
			is Value.Argument -> null
			is Value.F32 -> null
			is Value.Struct -> null
			is Value.StructAt -> (lhs as? Value.Struct)?.let { lhs ->
				lhs.fields.firstOrNull { it.name == name }?.value
			}
			is Value.Switch -> null
			is Value.Plus -> when (lhs) {
				is Value.F32 -> (rhs as? Value.F32)?.let { Value.F32(lhs.float + rhs.float) }
				else -> null
			}
			is Value.Minus -> when (lhs) {
				is Value.F32 -> (rhs as? Value.F32)?.let { Value.F32(lhs.float - rhs.float) }
				else -> null
			}
			is Value.Times -> when (lhs) {
				is Value.F32 -> (rhs as? Value.F32)?.let { Value.F32(lhs.float * rhs.float) }
				else -> null
			}
			is Value.Call -> null
		} ?: this

val Value.Field.optimize
	get() =
		Value.Field(name, value.optimize)

val Value.innerOptimize: Value
	get() =
		when (this) {
			is Value.Argument -> this
			is Value.F32 -> this
			is Value.Struct -> Value.Struct(fields.map { it.optimize })
			is Value.StructAt -> Value.StructAt(lhs.optimize, name)
			is Value.Switch -> Value.Switch(lhs, functions.map { it.optimize })
			is Value.Plus -> Value.Plus(lhs.optimize, rhs.optimize)
			is Value.Minus -> Value.Minus(lhs.optimize, rhs.optimize)
			is Value.Times -> Value.Times(lhs.optimize, rhs.optimize)
			is Value.Call -> Value.Call(function.optimize, param.optimize)
		}

val Value.Function.optimize
	get() =
		Value.Function(param, body.optimize)