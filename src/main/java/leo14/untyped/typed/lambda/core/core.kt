package leo14.untyped.typed.lambda.core

import leo14.Literal
import leo14.spacedString
import leo14.untyped.typed.javaValue
import leo14.untyped.typed.lambda.Typed
import leo14.untyped.typed.lambda.core.java.apply
import leo14.untyped.typed.lambda.core.java.compiledJavaCore
import leo14.untyped.typed.lambda.core.java.runtimeJavaCore
import leo14.untyped.typed.valueLiteralOrNull

class Core(
	val literalTermFn: (Literal) -> Any?,
	val termLiteralFn: (Any?) -> Literal?,
	val applyFn: (Typed) -> Typed?)

val runtimeCore = Core(
	literalTermFn = Literal::javaValue,
	termLiteralFn = Any?::valueLiteralOrNull,
	applyFn = runtimeJavaCore::apply)

val compiledCore = Core(
	literalTermFn = { it.spacedString },
	termLiteralFn = { null!! },
	applyFn = compiledJavaCore::apply)