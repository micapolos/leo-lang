package leo13.compiler

import leo13.LeoObject
import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.script
import leo13.type.*
import leo13.value.*

data class Compiled(val expr: Expr, val type: Type) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiled"
	override val scriptableBody get() = script(expr.scriptableLine, type.scriptableLine)
}

fun compiled(expr: Expr, type: Type) = Compiled(expr, type)
fun compiled() = compiled(expr(value()), type())

fun compiled(script: Script): Compiled =
	compiled(script.expr, script.type)

fun compiled(scriptLine: ScriptLine): CompiledLine =
	scriptLine.name lineTo compiled(scriptLine.rhs)

fun Compiled.plus(compiledLine: CompiledLine): Compiled =
	compiled(
		expr.plus(op(compiledLine.name lineTo compiledLine.rhs.expr)),
		type.plus(compiledLine.name lineTo compiledLine.rhs.type))

val Compiled.lhsOrNull: Compiled?
	get() =
		type
			.previousOrNull
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
		.linkOrNull
		?.let { typeLink ->
			if (typeLink.line.name == name)
				compiled(
					expr.plus(op(rhsLine)),
					type(typeLink.line))
			else compiled(
				expr.plus(op(lhs)),
				typeLink.lhs)
				.lineOrNull(name)
		}

fun Compiled.getOrNull(name: String): Compiled? =
	type
		.getOrNull(name)
		?.let { type ->
			compiled(expr.plus(op(get(name))), type)
		}

fun Compiled.setOrNull(line: CompiledLine): Compiled? =
	type
		.setOrNull(line.name lineTo line.rhs.type)
		?.let { setType ->
			compiled(
				expr.plus(op(set(line.name lineTo line.rhs.expr))),
				setType)
		}
