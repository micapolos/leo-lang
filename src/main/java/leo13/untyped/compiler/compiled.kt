package leo13.untyped.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.pattern.*

data class Compiled(val expression: Expression, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"compiled" lineTo script(expression.scriptLine, pattern.scriptLine)
}

fun compiled(expression: Expression, pattern: Pattern) =
	Compiled(expression, pattern)

fun compiled(vararg lines: CompiledLine) =
	Compiled(expression(), pattern()).fold(lines) { plus(it) }

fun compiled(script: Script): Compiled =
	TODO()

fun Compiled.plus(line: CompiledLine) =
	compiled(
		expression.plus(line.op),
		pattern.plus(line.patternLine))

val Compiled.previousOrNull: Compiled?
	get() =
		pattern
			.previousOrNull
			?.run { compiled(expression.plus(previous.op), this) }

val Compiled.everythingOrNull: Compiled?
	get() =
		pattern
			.everythingOrNull
			?.run { compiled(expression.plus(everything.op), this) }

fun Compiled.getOrNull(name: String): Compiled? =
	pattern
		.getOrNull(name)
		?.run {
			compiled(
				expression.plus(get(name).op),
				this)
		}

fun Compiled.plusIn(rhs: Compiled): Compiled =
	compiled(
		expression.plus(bind(rhs.expression).op),
		rhs.pattern)
