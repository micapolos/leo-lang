package leo13.untyped.compiler

import leo.base.fold
import leo13.script.Script
import leo13.untyped.expression.*
import leo13.untyped.pattern.Pattern
import leo13.untyped.pattern.pattern
import leo13.untyped.pattern.plus
import leo13.untyped.pattern.previousOrNull

data class Compiled(val expression: Expression, val pattern: Pattern)

fun compiled(expression: Expression, pattern: Pattern) =
	Compiled(expression, pattern)

fun compiled(vararg lines: CompiledLine) =
	Compiled(expression(), pattern()).fold(lines) { plus(it) }

fun compiled(script: Script): Compiled = TODO()

fun Compiled.plus(line: CompiledLine) =
	compiled(
		expression.plus(line.op),
		pattern.plus(line.patternLine))

val Compiled.previousOrNull: Compiled?
	get() =
		pattern
			.previousOrNull
			?.run { compiled(expression.plus(previous.op), this) }

fun Compiled.plus(switch: CompiledSwitch): Compiled =
	TODO()
//	compiled(
//		expression.plus(switch),
//		switch.caseStack.valueOrNull?.rhs?.pattern?:pattern)