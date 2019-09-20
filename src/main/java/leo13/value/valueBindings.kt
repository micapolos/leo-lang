package leo13.value

import leo13.*

data class ValueBindings(val stack: Stack<Value>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bindings"
	override val scriptableBody get() = stack.asScript
}

val Stack<Value>.valueBindings get() = ValueBindings(this)
fun ValueBindings.push(value: Value) = stack.push(value).valueBindings
fun valueBindings(vararg values: Value) = stack(*values).valueBindings

fun ValueBindings.evaluate(expr: Expr): Value =
	evaluator(this, value()).push(expr).value

fun ValueBindings.evaluate(exprLine: ExprLine): ValueLine =
	exprLine.name lineTo evaluate(exprLine.rhs)

fun ValueBindings.evaluate(op: Op): Value =
	evaluator(this, value()).push(op).value

fun ValueBindings.valueOrNull(given: Given): Value? =
	stack.drop(given.previousStack)?.linkOrNull?.value
