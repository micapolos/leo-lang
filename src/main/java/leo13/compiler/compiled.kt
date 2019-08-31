package leo13.compiler

import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.script
import leo13.type.*
import leo13.value.*

data class Compiled(val expr: Expr, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiled"
	override val scriptableBody get() = script(expr.scriptableLine, type.scriptableLine)
}

fun compiled(expr: Expr, type: Type) = Compiled(expr, type)
fun compiled() = compiled(expr(value()), type())

fun compiled(script: Script): Compiled =
	compiled(script.expr, type(script.pattern))

fun Compiled.plus(compiledLine: CompiledLine): Compiled =
	compiled(
		expr.plus(op(compiledLine.name lineTo compiledLine.rhs.expr)),
		type.updatePattern {
			plus(compiledLine.name lineTo compiledLine.rhs.type.pattern)
		})

val Compiled.lhsOrNull: Compiled?
	get() =
		type
			.lhsOrNull
			?.let { lhsTrace ->
				compiled(
					expr.plus(op(lhs)),
					lhsTrace)
			}

val Compiled.lineOrNull: Compiled?
	get() =
		type
			.lineOrNull
			?.let { lineTrace ->
				compiled(
					expr.plus(op(rhsLine)),
					lineTrace)
			}

val Compiled.rhsOrNull: Compiled?
	get() =
		type
			.rhsOrNull
			?.let { rhsTrace ->
				compiled(
					expr.plus(op(rhs)),
					rhsTrace)
			}

fun Compiled.lineOrNull(name: String): Compiled? =
	type
		.pattern
		.linkOrNull
		?.let { typeLink ->
			if (typeLink.line.name == name)
				compiled(
					expr.plus(op(rhsLine)),
					type.set(pattern(typeLink.line)))
			else compiled(
				expr.plus(op(lhs)),
				type.set(typeLink.lhs))
				.lineOrNull(name)
		}

fun Compiled.accessOrNull(name: String): Compiled? =
	type
		.pattern
		.onlyLineOrNull
		?.let { onlyLine ->
			type.applyOrNull(onlyLine.rhs)?.let { rhsType ->
				compiled(expr.plus(op(rhs)), rhsType).lineOrNull(name)
			}
		}
