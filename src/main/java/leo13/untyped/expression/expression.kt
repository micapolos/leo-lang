package leo13.untyped.expression

import leo.base.fold
import leo13.*
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.value.Value

data class Expression(val opStack: Stack<Op>)

val Stack<Op>.expression get() = Expression(this)

fun expression(vararg ops: Op) = stack(*ops).expression

fun expression(line: ExpressionLine, vararg lines: ExpressionLine) =
	expression().plus(line).fold(lines) { plus(it) }

fun expression(name: String) = expression(plus(name lineTo expression()).op)

val Expression.scriptLine
	get() =
		expressionName lineTo bodyScript.emptyIfEmpty

val Expression.bodyScript
	get() =
		opStack.map { bodyScriptLine }.script

fun Expression.plus(op: Op): Expression =
	opStack.push(op).expression

fun Expression.plus(line: ExpressionLine): Expression =
	opStack.push(leo13.untyped.expression.plus(line).op).expression

fun Expression.evaluate(value: Value): Value =
	valueContext().give(value).evaluator().plus(this).evaluated.value
