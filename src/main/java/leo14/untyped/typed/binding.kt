package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Binding(val fromType: Value, val toTyped: Typed)

fun binding(fromType: Value, toTyped: Typed) = Binding(fromType, toTyped)

fun Binding.apply(typed: Typed): Typed? =
	typed.cast(fromType)?.run { toTyped }
