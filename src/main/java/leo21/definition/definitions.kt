package leo21.definition

import leo13.Stack
import leo13.fold
import leo13.map
import leo13.push
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.reflectOrEmptyScriptLine
import leo14.script
import leo21.compiled.Compiled

data class Definitions(val definitionStack: Stack<Definition>) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = definitionStack.reflectOrEmptyScriptLine("definitions") { reflectScriptLine }
}

val Stack<Definition>.asBindings get() = Definitions(this)
val emptyDefinitions = Definitions(stack())
fun Definitions.plus(definition: Definition): Definitions = definitionStack.push(definition).asBindings

fun Compiled.wrap(definitions: Definitions): Compiled =
	fold(definitions.definitionStack) { wrap(it) }

val Definitions.printScript: Script
	get() =
		definitionStack.map { printScriptLine }.script
