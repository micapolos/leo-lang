package leo13.compiler

import leo13.ObjectScripting
import leo13.contextName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class TypeContext(
	val definitions: TypeDefinitions,
	val trace: NameTrace) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = contextName lineTo script(
			definitions.scriptingLine,
			trace.scriptingLine)

	fun plus(definition: TypeDefinition) =
		copy(definitions = definitions.plus(definition))

	fun trace(name: String) =
		copy(trace = trace.plus(name))
}

fun typeContext() = TypeContext(typeDefinitions(), nameTrace())
fun typeContext(context: Context) = TypeContext(context.typeDefinitions, nameTrace())
