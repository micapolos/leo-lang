package leo13.compiler

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.pattern.Pattern
import leo13.pattern.PatternLine
import leo13.script.lineTo
import leo13.script.script

data class PatternDefinition(
	val line: PatternLine,
	val hasPattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() = "definition" lineTo script(
			line.scriptingLine,
			"has" lineTo script(hasPattern.scriptingLine))

	fun hasPatternOrNull(line: PatternLine): Pattern? =
		notNullIf(this.line == line) {
			hasPattern
		}

	fun resolveOrNull(line: PatternLine): PatternLine? =
		hasPatternOrNull(line)?.let {
			line.leafPlusOrNull(it)
		}
}

fun definition(line: PatternLine, hasPattern: Pattern) =
	PatternDefinition(line, hasPattern)

