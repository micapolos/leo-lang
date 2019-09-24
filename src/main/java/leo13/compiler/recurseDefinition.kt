package leo13.compiler

import leo.base.ifOrNull
import leo13.ObjectScripting
import leo13.definitionName
import leo13.pattern.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class RecurseDefinition(val line: PatternLine, val recurse: Recurse) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = definitionName lineTo script(line.scriptingLine, recurse.scriptingLine)

	fun resolve(line: PatternLine): Pattern? =
		ifOrNull(this.line == line) { pattern(recurse) }

	fun resolve(pattern: Pattern): Pattern? =
		pattern.linkOrNull?.onlyLineOrNull?.let { resolve(it) }

	val recurseIncrease
		get() =
			RecurseDefinition(line, recurse.increase)
}

fun definition(line: PatternLine, recurse: Recurse) = RecurseDefinition(line, recurse)

fun RecurseDefinition?.orNullResolve(pattern: Pattern) =
	this?.resolve(pattern)?:pattern