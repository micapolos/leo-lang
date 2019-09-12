package leo13.untyped.expression

import leo13.untyped.evaluator.Value
import leo9.Stack
import leo9.drop
import leo9.push
import leo9.valueOrNull

data class Bindings(val valueStack: Stack<Value>)

val Stack<Value>.bindings
	get() =
		Bindings(this)

fun Bindings.plus(value: Value) =
	valueStack.push(value).bindings

fun Bindings.valueOrNull(bound: Bound): Value? =
	valueStack.drop(bound.previousStack)?.valueOrNull

fun Bindings.evaluate(expression: Expression): Value =
	evaluator().plus(expression).value