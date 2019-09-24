package leo13.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.lineName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class PatternLine(val name: String, val unexpandedRhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			lineName lineTo script(name lineTo unexpandedRhs.scriptingLine.rhs)

	val rhs get() = expand().unexpandedRhs

	fun rhsOrNull(name: String) =
		notNullIf(this.name == name) { rhs }

	fun setRhsOrNull(line: PatternLine): PatternLine? =
		notNullIf(name == line.name) { line }

	fun leafPlusOrNull(pattern: Pattern): PatternLine? =
		rhs.leafPlusOrNull(pattern)?.let { name lineTo it }

	val onlyNameOrNull: String? get() = notNullIf(unexpandedRhs.isEmpty) { name }

	fun expand(rootOrNull: RecurseRoot? = null): PatternLine =
		name lineTo unexpandedRhs.expand(rootOrNull.orNullRecurseIncrease(this))

	fun contains(line: PatternLine, traceOrNull: PatternTrace?): Boolean =
		name == line.name && unexpandedRhs.contains(line.unexpandedRhs, traceOrNull)
}

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

val ScriptLine.patternLine: PatternLine get() = name lineTo rhs.pattern

val String.patternLine: PatternLine get() = lineTo(pattern())