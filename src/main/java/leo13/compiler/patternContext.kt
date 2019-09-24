package leo13.compiler

import leo13.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine

data class PatternContext(
	val definitions: PatternDefinitions,
	val tracedNameStack: Stack<String>) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = contextName lineTo script(
			definitions.scriptingLine,
			tracedName lineTo script(namesName lineTo tracedNameStack.map { scriptLine }.script))

	fun plus(definition: PatternDefinition) =
		copy(definitions = definitions.plus(definition))

	fun trace(name: String) =
		copy(tracedNameStack = tracedNameStack.push(name))
}

fun patternContext() = PatternContext(patternDefinitions(), stack())
fun patternContext(context: Context) = PatternContext(context.patternDefinitions, stack())
