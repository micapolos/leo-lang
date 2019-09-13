package leo13.untyped.expression

import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expressionName
import leo13.untyped.value.Value
import leo9.Stack
import leo9.map
import leo9.push
import leo9.stack

data class Expression(val opStack: Stack<Op>)

val Stack<Op>.expression get() = Expression(this)

fun expression(vararg ops: Op) = stack(*ops).expression

val Expression.scriptLine
	get() =
		expressionName lineTo bodyScript.emptyIfEmpty

val Expression.bodyScript
	get() =
		opStack.map { bodyScriptLine }.script

fun Expression.plus(op: Op): Expression =
	opStack.push(op).expression

fun Expression.evaluate(value: Value): Value =
	given(value).evaluator().plus(this).evaluated.value