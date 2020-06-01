package leo16

import leo13.Stack
import leo13.mapFirst
import leo13.push

data class TypedDictionary(val definitionStack: Stack<TypedDefinition>)

val Stack<TypedDefinition>.dictionary get() = TypedDictionary(this)
fun TypedDictionary.plus(definition: TypedDefinition) = definitionStack.push(definition).dictionary

fun TypedDictionary.apply(typed: Typed): Typed? =
	definitionStack.mapFirst { apply(typed) }

fun TypedDictionary.resolve(typed: Typed): Typed =
	apply(typed) ?: typed.resolve
