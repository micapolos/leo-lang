package leo25

import leo13.Stack
import leo13.push
import leo13.stack

data class Library(val definitionStack: Stack<Definition>)

fun library(definitionStack: Stack<Definition>) = Library(definitionStack)
fun library(vararg definitions: Definition) = library(stack(*definitions))
operator fun Library.plus(definition: Definition) = library(definitionStack.push(definition))
