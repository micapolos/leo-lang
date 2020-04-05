package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Typed(
	val type: Value,
	val value: Value)

val emptyTyped = Typed(null, null)

val Typed.script get() = value.valueScript