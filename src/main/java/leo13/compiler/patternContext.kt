package leo13.compiler

import leo13.ObjectScripting
import leo13.contextName
import leo13.emptyName
import leo13.pattern.PatternLine
import leo13.pattern.PatternTrace
import leo13.pattern.orNullPlus
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class PatternContext(
	val definitions: PatternDefinitions,
	val traceOrNull: PatternTrace?) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = contextName lineTo script(
			definitions.scriptingLine,
			traceOrNull?.scriptingLine ?: "trace" lineTo script(emptyName))

	fun plus(definition: PatternDefinition) =
		copy(definitions = definitions.plus(definition))

	fun trace(line: PatternLine) =
		copy(traceOrNull = traceOrNull?.orNullPlus(line))
}

fun patternContext() = PatternContext(patternDefinitions(), null)
