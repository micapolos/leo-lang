package leo14.typed.compiler

import leo14.Literal
import leo14.typed.Typed
import leo14.typed.TypedLine

data class Context<T>(
	val dictionary: Dictionary,
	val typedResolve: Typed<T>.() -> Typed<T>?,
	val literalCompile: Literal.() -> TypedLine<T>)

fun <T> Context<T>.compileLine(literal: Literal): TypedLine<T> =
	literal.literalCompile()

