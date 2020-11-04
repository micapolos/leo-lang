package leo21.dictionary

import leo13.Stack
import leo13.push
import leo13.stack
import leo14.Script
import leo21.compiled.Bindings

data class Dictionary(val definitionStack: Stack<Definition>)

val emptyDictionary = Dictionary(stack())
fun Dictionary.plus(definition: Definition) = Dictionary(definitionStack.push(definition))

fun Bindings.dictionary(script: Script): Dictionary =
	emptyDictionaryCompiler.plus(script).dictionary