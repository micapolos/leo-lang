package leo14.typed.compiler

import leo14.Language
import leo14.Literal
import leo14.lambda.Evaluator
import leo14.typed.DecompileLiteral
import leo14.typed.Typed
import leo14.typed.TypedLine

typealias TypedResolve<T> = Typed<T>.() -> Typed<T>?

data class Context<T>(
	val language: Language,
	val typedResolve: TypedResolve<T>,
	val literalCompile: LiteralCompile<T>,
	val evaluator: Evaluator<T>,
	val typeContext: TypeContext,
	val decompileLiteral: DecompileLiteral<T>)

fun <T> Context<T>.compileLine(literal: Literal): TypedLine<T> =
	literal.literalCompile()

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	typedResolve.invoke(typed)
