package leo14.typed.compiler

import leo14.Literal
import leo14.any
import leo14.lambda.term
import leo14.typed.*

val anyLiteralCompile: Literal.() -> TypedLine<Any> = { term(any) of nativeLine }
val anyResolve: Typed<Any>.() -> Typed<Any>? = { null }

data class Context<T>(
	val dictionary: Dictionary,
	val memory: Memory<T>,
	val typedResolve: Typed<T>.() -> Typed<T>?,
	val literalCompile: Literal.() -> TypedLine<T>)

fun anyContext(memory: Memory<Any>) =
	Context(englishDictionary, memory, anyResolve, anyLiteralCompile)

val anyContext: Context<Any> = anyContext(anyMemory())

val anyPolishContext: Context<Any> = Context(polishDictionary, anyMemory(), anyResolve, anyLiteralCompile)

fun <T> Context<T>.compileLine(literal: Literal): TypedLine<T> =
	literal.literalCompile()

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	memory
		.indexedItem(typed.type)
		?.let { (index, item) ->
			item.resolve(index, typed.term)
		}

fun <T> Context<T>.plus(typed: Typed<T>, typedField: TypedField<T>): Typed<T> =
	typed.plus(typedField).let { plused ->
		resolve(plused) ?: plused.typedResolve() ?: typed.eval(typedField)
	}

fun <T> Context<T>.ret(typed: Typed<T>): Typed<T> =
	memory.ret(typed)