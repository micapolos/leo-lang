package leo13.untyped.compiler

import leo.base.fold
import leo13.ObjectScripting
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.pattern.*

data class ExpressionCompiled(val expression: Expression, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"compiled" lineTo script(expression.scriptLine, pattern.scriptLine)
}

fun compiled(expression: Expression, pattern: Pattern) =
	ExpressionCompiled(expression, pattern)

fun compiled(vararg lines: CompiledLine) =
	ExpressionCompiled(expression(), pattern()).fold(lines) { plus(it) }

fun compiled(script: Script): ExpressionCompiled =
	TODO()

fun ExpressionCompiled.plus(line: CompiledLine) =
	compiled(
		expression.plus(line.op),
		pattern.plus(line.patternLine))

val ExpressionCompiled.previousOrNull: ExpressionCompiled?
	get() =
		pattern
			.previousOrNull
			?.run { compiled(expression.plus(previous.op), this) }

fun ExpressionCompiled.plus(switch: CompiledSwitch): ExpressionCompiled =
	TODO()
//	compiled(
//		expression.plus(switch),
//		switch.caseStack.valueOrNull?.rhs?.pattern?:pattern)

fun ExpressionCompiled.getOrNull(name: String): ExpressionCompiled? =
	pattern
		.getOrNull(name)
		?.run {
			compiled(
				expression.plus(get(name).op),
				this)
		}