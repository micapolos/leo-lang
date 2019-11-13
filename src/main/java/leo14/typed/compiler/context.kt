package leo14.typed.compiler

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirstIndexed
import leo13.push
import leo13.stack
import leo14.Literal
import leo14.any
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.typed.Arrow
import leo14.typed.Typed
import leo14.typed.of

data class Context<T>(
	val rememberedArrowStack: Stack<Arrow>,
	val literalCompile: Literal.() -> T)

val anyContext: Context<Any> = Context(stack()) { any }

fun <T> Context<T>.compile(literal: Literal): T =
	literal.literalCompile()

fun <T> Context<T>.remember(arrow: Arrow): Context<T> =
	copy(rememberedArrowStack = rememberedArrowStack.push(arrow))

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	rememberedArrowStack
		.mapFirstIndexed {
			notNullIf(lhs == typed.type) { rhs }
		}
		?.let { (index, rhs) ->
			arg<T>(index).invoke(typed.term) of rhs
		}