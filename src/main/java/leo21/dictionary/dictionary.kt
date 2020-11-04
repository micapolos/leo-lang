package leo21.dictionary

import leo13.Stack
import leo13.push
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.reflectOrEmptyScriptLine
import leo21.compiler.Bindings

data class Dictionary(val definitionStack: Stack<Definition>) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = definitionStack.reflectOrEmptyScriptLine("dictionary") { reflectScriptLine }
}

val emptyDictionary = Dictionary(stack())
fun Dictionary.plus(definition: Definition) = Dictionary(definitionStack.push(definition))

fun Bindings.dictionary(script: Script): Dictionary =
	emptyDictionaryCompiler.plus(script).dictionary