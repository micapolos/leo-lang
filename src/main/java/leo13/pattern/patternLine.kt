package leo13.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class PatternLine(val name: String, val rhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"line" lineTo script(name lineTo rhs.scriptingLine.rhs)

	fun contains(line: PatternLine) =
		name == line.name && rhs.contains(line.rhs)

	fun rhsOrNull(name: String) =
		notNullIf(this.name == name) { rhs }

	fun setRhsOrNull(line: PatternLine): PatternLine? =
		notNullIf(name == line.name) { line }

	fun leafPlusOrNull(pattern: Pattern): PatternLine? =
		rhs.leafPlusOrNull(pattern)?.let { name lineTo it }

	val onlyNameOrNull: String? get() = notNullIf(rhs.isEmpty) { name }
}

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

val ScriptLine.patternLine: PatternLine get() = name lineTo rhs.pattern