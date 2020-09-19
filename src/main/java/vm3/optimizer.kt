package vm3

val Value.optimize: Value
	get() =
		innerOptimize.outerOptimize

val Value.outerOptimize: Value
	get() =
		when (this) {
			is Value.Argument -> null
			is Value.Bool -> null
			is Value.I32 -> null
			is Value.F32 -> null
			is Value.Array -> null
			is Value.ArrayAt -> (lhs as? Value.Array)?.let { lhs ->
				(index as? Value.I32)?.let { index ->
					lhs.items.getOrNull(index.int)
				}
			}
			is Value.Struct -> null
			is Value.StructAt -> (lhs as? Value.Struct)?.let { lhs ->
				lhs.fields.firstOrNull { it.name == name }?.value
			}
			is Value.Switch -> null
			is Value.Inc -> when (lhs) {
				is Value.I32 -> Value.I32(lhs.int.inc())
				else -> null
			}
			is Value.Dec -> when (lhs) {
				is Value.I32 -> Value.I32(lhs.int.dec())
				else -> null
			}
			is Value.Plus -> when (lhs) {
				is Value.I32 -> (rhs as? Value.I32)?.let { Value.I32(lhs.int + rhs.int) }
				is Value.F32 -> (rhs as? Value.F32)?.let { Value.F32(lhs.float + rhs.float) }
				else -> null
			}
			is Value.Minus -> when (lhs) {
				is Value.I32 -> (rhs as? Value.I32)?.let { Value.I32(lhs.int - rhs.int) }
				is Value.F32 -> (rhs as? Value.F32)?.let { Value.F32(lhs.float - rhs.float) }
				else -> null
			}
			is Value.Times -> when (lhs) {
				is Value.I32 -> (rhs as? Value.I32)?.let { Value.I32(lhs.int * rhs.int) }
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
			is Value.Bool -> this
			is Value.I32 -> this
			is Value.F32 -> this
			is Value.Array -> Value.Array(items.map { it.optimize })
			is Value.ArrayAt -> Value.ArrayAt(lhs.optimize, index.optimize)
			is Value.Struct -> Value.Struct(fields.map { it.optimize })
			is Value.StructAt -> Value.StructAt(lhs.optimize, name)
			is Value.Switch -> Value.Switch(lhs, cases.map { it.optimize })
			is Value.Inc -> Value.Inc(lhs.optimize)
			is Value.Dec -> Value.Dec(lhs.optimize)
			is Value.Plus -> Value.Plus(lhs.optimize, rhs.optimize)
			is Value.Minus -> Value.Minus(lhs.optimize, rhs.optimize)
			is Value.Times -> Value.Times(lhs.optimize, rhs.optimize)
			is Value.Call -> Value.Call(function.optimize, param.optimize)
		}

val Value.Function.optimize
	get() =
		Value.Function(param, body.optimize)