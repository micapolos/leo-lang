package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Rule(val fromType: Value, val toType: Value, val valueFn: Fn)

fun rule(fromType: Value, toType: Value, fn: Fn) = Rule(fromType, toType, fn)

fun Rule.apply(typed: Typed): Typed? =
	typed.castValueFn(fromType)?.let { castValueFn ->
		typed(toType) {
			valueFn(castValueFn.invoke())
		}
	}
