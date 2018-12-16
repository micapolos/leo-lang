package leo.term

import leo.base.string

sealed class Expander

data class OneOfExpander(
	val oneOf: OneOf) : Expander() {
	override fun toString() = oneOf.string
}

data class RecursionExpander(
	val recursion: Recursion) : Expander() {
	override fun toString() = recursion.string
}

// === constructors

val Recursion.expander: Expander
	get() =
		RecursionExpander(this)

val OneOf.expander: Expander
	get() =
		OneOfExpander(this)

fun expander(oneOf: OneOf): Expander =
	oneOf.expander

fun expander(recursion: Recursion): Expander =
	recursion.expander