package leo13.compiler

import leo.base.notNullIf
import leo13.*
import leo13.pattern.PatternLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo

data class PatternLines(val stack: Stack<PatternLine>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "patterns" lineTo stack.scripting.script.emptyIfEmpty
	fun plus(line: PatternLine) = PatternLines(stack.push(line))

	fun resolve(line: CompiledLine): CompiledLine =
		stack.mapFirst {
			notNullIf(contains(line.patternLine)) {
				line.name lineTo compiled(line.rhs.expression, rhs)
			}
		} ?: line
}

fun patternLines() = PatternLines(stack())

