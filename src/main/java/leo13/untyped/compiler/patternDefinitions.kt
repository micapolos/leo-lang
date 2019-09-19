package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.scripting
import leo13.untyped.pattern.PatternLine
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

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
