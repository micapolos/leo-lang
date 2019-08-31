package leo13.compiler

import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.script.Scriptable
import leo13.script.script
import leo13.type.linkOrNull
import leo13.type.onlyLineOrNull
import leo13.type.type
import leo13.value.Expr
import leo13.value.op
import leo13.value.plus

data class TracedExpr(val expr: Expr, val trace: Trace) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "traced"
	override val scriptableBody get() = script(expr.scriptableLine, trace.scriptableLine)
}

fun traced(expr: Expr, trace: Trace) = TracedExpr(expr, trace)

val TracedExpr.lhsOrNull: TracedExpr?
	get() =
		trace
			.lhsOrNull
			?.let { lhsTrace ->
				traced(
					expr.plus(op(lhs)),
					lhsTrace)
			}

val TracedExpr.rhsOrNull: TracedExpr?
	get() =
		trace
			.rhsOrNull
			?.let { rhsTrace ->
				traced(
					expr.plus(op(rhs)),
					rhsTrace)
			}

fun TracedExpr.lineOrNull(name: String): TracedExpr? =
	trace
		.type
		.linkOrNull
		?.let { typeLink ->
			if (typeLink.line.name == name)
				traced(
					expr.plus(op(rhsLine)),
					trace.set(type(typeLink)))
			else traced(
				expr.plus(op(lhs)),
				trace.set(typeLink.lhs))
				.lineOrNull(name)
		}

fun TracedExpr.accessOrNull(name: String): TracedExpr? =
	trace
		.type
		.onlyLineOrNull
		?.let { onlyLine ->
			traced(
				expr.plus(op(rhs)),
				trace.push(type(onlyLine)))
				.lineOrNull(name)
		}
