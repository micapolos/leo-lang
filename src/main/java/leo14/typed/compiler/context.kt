package leo14.typed.compiler

import leo14.Literal
import leo14.any
import leo14.typed.*

val anyLiteralCompile: Literal.() -> Any = { any }

data class Context<T>(
	val dictionary: Dictionary,
	val memory: Memory<T>,
	val resolver: Resolver<T>,
	val literalCompile: Literal.() -> T)

fun anyContext(memory: Memory<Any>) =
	Context(englishDictionary, memory, anyResolver, anyLiteralCompile)

val anyContext: Context<Any> = anyContext(anyMemory())

val anyPolishContext: Context<Any> = Context(polishDictionary, anyMemory(), anyResolver, anyLiteralCompile)

fun <T> Context<T>.compile(literal: Literal): T =
	literal.literalCompile()

fun <T> Context<T>.remember(action: Action<T>): Context<T> =
	copy(memory = memory.plus(RememberMemoryItem(action, needsInvoke = true)))

fun <T> Context<T>.resolve(typed: Typed<T>): Typed<T>? =
	memory
		.indexedItem(typed.type)
		?.let { (index, item) ->
			item.resolve(index, typed.term)
		}

fun <T> Context<T>.plus(typed: Typed<T>, typedField: TypedField<T>): Typed<T> =
	typed.plus(typedField).let { plused ->
		resolve(plused) ?: resolver.resolve(typed) ?: typed.eval(typedField)
	}

fun <T> Context<T>.ret(typed: Typed<T>): Typed<T> =
	memory.ret(typed)