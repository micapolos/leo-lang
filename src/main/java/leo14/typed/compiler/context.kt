package leo14.typed.compiler

import leo14.Literal
import leo14.lambda.NativeApply
import leo14.typed.Typed
import leo14.typed.TypedLine

typealias TypedResolve<T> = Typed<T>.() -> Typed<T>?
typealias LiteralCompile<T> = Literal.() -> TypedLine<T>

data class Context<T>(
	val dictionary: Dictionary,
	val typedResolve: TypedResolve<T>,
	val literalCompile: LiteralCompile<T>,
	val nativeApply: NativeApply<T>)

fun <T> Context<T>.compileLine(literal: Literal): TypedLine<T> =
	literal.literalCompile()

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	typedResolve.invoke(typed)