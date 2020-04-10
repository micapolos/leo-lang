package leo14.untyped.typed.lambda.core

import leo14.Literal
import leo14.untyped.typed.Compiled
import leo14.untyped.typed.apply
import leo14.untyped.typed.nativeValue
import leo14.untyped.typed.valueLiteralOrNull

class Core(
	val literalTermFn: (Literal) -> Any?,
	val termLiteralFn: (Any?) -> Literal?,
	val applyFn: (Compiled) -> Compiled?)

val runtimeCore = Core(
	literalTermFn = Literal::nativeValue,
	termLiteralFn = Any?::valueLiteralOrNull,
	applyFn = Compiled::apply)
