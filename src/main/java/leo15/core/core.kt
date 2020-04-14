package leo15.core

import leo14.Literal
import leo14.spacedString
import leo14.untyped.typed.javaValue
import leo14.untyped.typed.valueLiteralOrNull
import leo15.Typed
import leo15.core.java.apply
import leo15.core.java.compiledJavaCore
import leo15.core.java.runtimeJavaCore

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