package leo13.compiler

import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.script.Scriptable
import leo13.script.script
import leo13.type.*
import leo13.value.Expr
import leo13.value.op
import leo13.value.plus

data class Compiled(val expr: Expr, val trace: Trace) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "traced"
	override val scriptableBody get() = script(expr.scriptableLine, trace.scriptableLine)
}

fun compiled(expr: Expr, trace: Trace) = Compiled(expr, trace)

val Compiled.lhsOrNull: Compiled?
	get() =
		trace
			.lhsOrNull
			?.let { lhsTrace ->
				compiled(
					expr.plus(op(lhs)),
					lhsTrace)
			}

val Compiled.rhsOrNull: Compiled?
	get() =
		trace
			.rhsOrNull
			?.let { rhsTrace ->
				compiled(
					expr.plus(op(rhs)),
					rhsTrace)
			}

fun Compiled.lineOrNull(name: String): Compiled? =
	trace
		.type
		.linkOrNull
		?.let { typeLink ->
			if (typeLink.line.name == name)
				compiled(
					expr.plus(op(rhsLine)),
					trace.set(type(typeLink)))
			else compiled(
				expr.plus(op(lhs)),
				trace.set(typeLink.lhs))
				.lineOrNull(name)
		}

fun Compiled.accessOrNull(name: String): Compiled? =
	trace
		.type
		.onlyLineOrNull
		?.let { onlyLine ->
			compiled(
				expr.plus(op(rhs)),
				trace.push(type(onlyLine)))
				.lineOrNull(name)
		}
