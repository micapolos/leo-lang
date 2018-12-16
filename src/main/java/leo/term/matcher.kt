package leo.term

import leo.base.string

sealed class Matcher

data class OneOfMatcher(
	val oneOf: OneOf) : Matcher() {
	override fun toString() = oneOf.string
}

data class RecursionMatcher(
	val recursion: Recursion) : Matcher() {
	override fun toString() = recursion.string
}

// === constructors

val Recursion.matcher: Matcher
	get() =
		RecursionMatcher(this)

val OneOf.matcher: Matcher
	get() =
		OneOfMatcher(this)

fun matcher(oneOf: OneOf): Matcher =
	oneOf.matcher

fun matcher(recursion: Recursion): Matcher =
	recursion.matcher