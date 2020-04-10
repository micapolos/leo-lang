package leo14.untyped.typed

import leo14.Literal

class Core(
	val literalTermFn: (Literal) -> Any?,
	val termLiteralFn: (Any?) -> Literal?,
	val applyFn: (Compiled) -> Compiled?)

val runtimeCore = Core(
	literalTermFn = Literal::nativeValue,
	termLiteralFn = Any?::valueLiteralOrNull,
	applyFn = Compiled::apply)
