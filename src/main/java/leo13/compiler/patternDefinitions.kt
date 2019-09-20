package leo13.compiler

import leo13.*
import leo13.pattern.PatternLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo

data class PatternDefinitions(val stack: Stack<PatternDefinition>) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"definitions" lineTo stack.scripting.script.emptyIfEmpty

	fun plus(definition: PatternDefinition) =
		PatternDefinitions(stack.push(definition))

	fun resolve(line: PatternLine): PatternLine =
		stack.mapFirst { resolveOrNull(line) } ?: line
}

fun patternDefinitions() = PatternDefinitions(stack())
