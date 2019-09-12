package leo13.untyped.expression

import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.bindingsName
import leo13.untyped.value.Value
import leo13.untyped.value.ValueLine
import leo13.untyped.value.lineTo
import leo13.untyped.value.scriptLine
import leo9.*

data class Bindings(val valueStack: Stack<Value>)

val Stack<Value>.bindings
	get() =
		Bindings(this)

fun bindings(vararg values: Value) =
	stack(*values).bindings

fun Bindings.plus(value: Value) =
	valueStack.push(value).bindings

fun Bindings.plus(bindings: Bindings) =
	fold(bindings.valueStack.reverse) { plus(it) }

fun Bindings.valueOrNull(bound: Bound): Value? =
	valueStack.drop(bound.previousStack)?.valueOrNull

fun Bindings.evaluate(expression: Expression): Value =
	evaluator().plus(expression).value

fun Bindings.evaluate(line: ExpressionLine): ValueLine =
	line.name lineTo evaluate(line.rhs)

val Bindings.scriptLine
	get() =
		bindingsName lineTo valueStack.script { scriptLine }.emptyIfEmpty