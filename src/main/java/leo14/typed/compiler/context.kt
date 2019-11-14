package leo14.typed.compiler

import leo.base.notNullIf
import leo13.*
import leo14.Literal
import leo14.any
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.typed.*

val anyLiteralCompile: Literal.() -> Any = { any }

data class Context<T>(
	val rememberedActionStack: Stack<Action<T>?>,
	val literalCompile: Literal.() -> T)

fun anyContext(rememberedActionStack: Stack<Action<Any>>) =
	Context(rememberedActionStack, anyLiteralCompile)

val anyContext: Context<Any> = anyContext(stack())

fun <T> Context<T>.compile(literal: Literal): T =
	literal.literalCompile()

fun <T> Context<T>.remember(action: Action<T>?): Context<T> =
	copy(rememberedActionStack = rememberedActionStack.push(action))

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	rememberedActionStack
		.mapFirstIndexed {
			this?.let {
				notNullIf(param == typed.type) { body.type }
			}
		}
		?.let { (index, rhs) ->
			arg<T>(index).invoke(typed.term) of rhs
		}


fun <T> Context<T>.plus(typed: Typed<T>, typedField: TypedField<T>): Typed<T> =
	typed.plus(typedField).let { plused ->
		resolve(plused) ?: typed.eval(typedField)
	}


fun <T> Context<T>.ret(typed: Typed<T>): Typed<T> =
	typed.fold(rememberedActionStack) {
		if (it != null) ret(it) else this
	}
