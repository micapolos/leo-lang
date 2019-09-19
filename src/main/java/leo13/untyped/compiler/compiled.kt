package leo13.untyped.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.pattern.*
import leo13.untyped.value.value

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

fun Compiled.plus(line: CompiledLine) =
	compiled(
		expression.plus(line.op),
		pattern.plus(line.patternLine))

fun Compiled.plus(name: String) =
	compiled(
		expression.plus(wrap(name).op),
		pattern(name lineTo pattern))

val Compiled.previousOrNull: Compiled?
	get() =
		pattern
			.previousOrNull
			?.run { compiled(expression.plus(previous.op), this) }

val Compiled.contentOrNull: Compiled?
	get() =
		pattern
			.contentOrNull
			?.run { compiled(expression.plus(content.op), this) }

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

fun Compiled.plus(switchCompiled: SwitchCompiled) =
	compiled(
		expression.plus(switchCompiled.switch.op),
		switchCompiled.pattern)

fun compiled(script: Script): Compiled =
	compiled(expression(script.value.op), pattern(script))

