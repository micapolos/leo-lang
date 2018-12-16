package leo.term

import leo.base.string

sealed class Type

data class OneOfType(
	val oneOf: OneOf) : Type() {
	override fun toString() = oneOf.string

}

data class RecursionType(
	val recursion: Recursion) : Type() {
	override fun toString() = recursion.string
}

// === constructors

val Recursion.type: Type
	get() =
		RecursionType(this)

val OneOf.type: Type
	get() =
		OneOfType(this)
