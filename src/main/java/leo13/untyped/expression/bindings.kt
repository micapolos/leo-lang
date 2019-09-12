package leo13.untyped.expression

import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.bindingsName
import leo13.untyped.evaluator.Value
import leo13.untyped.evaluator.scriptLine
import leo9.*

data class Bindings(val valueStack: Stack<Value>)

val Stack<Value>.bindings
	get() =
		Bindings(this)

fun bindings(vararg values: Value) =
	stack(*values).bindings

fun Bindings.plus(value: Value) =
	valueStack.push(value).bindings

fun Bindings.valueOrNull(bound: Bound): Value? =
	valueStack.drop(bound.previousStack)?.valueOrNull

fun Bindings.evaluate(expression: Expression): Value =
	evaluator().plus(expression).value

val Bindings.scriptLine
	get() =
		bindingsName lineTo valueStack.script { scriptLine }.emptyIfEmpty