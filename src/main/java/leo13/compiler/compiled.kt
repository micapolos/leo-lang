package leo13.compiler

import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.script
import leo13.type.*
import leo13.value.*

data class Compiled(val expr: Expr, val trace: Trace) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "traced"
	override val scriptableBody get() = script(expr.scriptableLine, trace.scriptableLine)
}

fun compiled(expr: Expr, trace: Trace) = Compiled(expr, trace)
fun compiled() = compiled(expr(), trace())

fun compiled(script: Script): Compiled =
	compiled(script.expr, trace(script.type))

fun Compiled.plus(compiledLine: CompiledLine): Compiled =
	compiled(
		expr(op(compiledLine.name lineTo compiledLine.rhs.expr)),
		trace.updateType {
			plus(compiledLine.name lineTo compiledLine.rhs.trace.type)
		})

val Compiled.lhsOrNull: Compiled?
	get() =
		trace
			.lhsOrNull
			?.let { lhsTrace ->
				compiled(
					expr.plus(op(lhs)),
					lhsTrace)
			}

val Compiled.lineOrNull: Compiled?
	get() =
		trace
			.lineOrNull
			?.let { lineTrace ->
				compiled(
					expr.plus(op(rhsLine)),
					lineTrace)
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
