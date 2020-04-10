package leo14.untyped.typed.lambda.core

import leo14.Literal
import leo14.spacedString
import leo14.untyped.typed.lambda.Compiled
import leo14.untyped.typed.lambda.core.java.apply
import leo14.untyped.typed.lambda.core.java.compiledJavaCore
import leo14.untyped.typed.lambda.core.java.runtimeJavaCore
import leo14.untyped.typed.nativeValue
import leo14.untyped.typed.valueLiteralOrNull

class Core(
	val literalTermFn: (Literal) -> Any?,
	val termLiteralFn: (Any?) -> Literal?,
	val applyFn: (Compiled) -> Compiled?)

val runtimeCore = Core(
	literalTermFn = Literal::nativeValue,
	termLiteralFn = Any?::valueLiteralOrNull,
	applyFn = runtimeJavaCore::apply)

val compiledCore = Core(
	literalTermFn = { it.spacedString },
	termLiteralFn = { null!! },
	applyFn = compiledJavaCore::apply)