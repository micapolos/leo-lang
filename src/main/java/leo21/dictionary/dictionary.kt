package leo21.dictionary

import leo13.Stack
import leo13.stack

data class Dictionary(val definitionStack: Stack<Definition>)

val emptyDictionary = Dictionary(stack())
fun Dictionary.plus(definition: Definition) = Dictionary(definitionStack)

