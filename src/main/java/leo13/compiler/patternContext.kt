package leo13.compiler

import leo13.ObjectScripting
import leo13.contextName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class PatternContext(
	val definitions: PatternDefinitions,
	val trace: NameTrace) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = contextName lineTo script(
			definitions.scriptingLine,
			trace.scriptingLine)

	fun plus(definition: PatternDefinition) =
		copy(definitions = definitions.plus(definition))

	fun trace(name: String) =
		copy(trace = trace.plus(name))
}

fun patternContext() = PatternContext(patternDefinitions(), nameTrace())
fun patternContext(context: Context) = PatternContext(context.patternDefinitions, nameTrace())
