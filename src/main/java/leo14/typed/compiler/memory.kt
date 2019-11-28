package leo14.typed.compiler

import leo.base.orNullIf
import leo13.*
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.typed.Function
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.of

data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)

sealed class MemoryItem<T>
data class EmptyMemoryItem<T>(val empty: Empty) : MemoryItem<T>()
data class RememberMemoryItem<T>(val function: Function<T>, val needsInvoke: Boolean) : MemoryItem<T>()

val <T> Stack<MemoryItem<T>>.memory get() = Memory(this)

fun <T> memory(vararg items: MemoryItem<T>) =
	Memory(stack(*items))

fun <T> memoryItem(empty: Empty): MemoryItem<T> =
	EmptyMemoryItem(empty)

fun <T> remember(function: Function<T>, needsInvoke: Boolean = true): MemoryItem<T> =
	RememberMemoryItem(function, needsInvoke)

fun anyMemory(): Memory<Any> =
	Memory(stack())

fun <T> Memory<T>.plus(item: MemoryItem<T>) =
	Memory(itemStack.push(item))

fun <T> Memory<T>.indexedItem(type: Type): Pair<Index, MemoryItem<T>>? =
	itemStack
		.mapFirstIndexed {
			orNullIf(!matches(type))
		}

fun <T> Memory<T>.resolve(typed: Typed<T>): Typed<T>? =
	indexedItem(typed.type)
		?.let { (index, item) ->
			item.resolve(index, typed.term)
		}

fun <T> MemoryItem<T>.matches(type: Type): Boolean =
	when (this) {
		is EmptyMemoryItem -> false
		is RememberMemoryItem -> function.takes == type
	}

fun <T> MemoryItem<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	when (this) {
		is EmptyMemoryItem -> error("Can not resolve this")
		is RememberMemoryItem ->
			if (needsInvoke) arg<T>(index).invoke(term) of function.does.type
			else arg<T>(index) of function.does.type
	}

fun <T> Memory<T>.ret(typed: Typed<T>): Typed<T> =
	typed.fold(itemStack) { ret(it) }

fun <T> Typed<T>.ret(item: MemoryItem<T>): Typed<T> =
	when (item) {
		is EmptyMemoryItem -> this
		is RememberMemoryItem -> fn(term).invoke(item.function.does.term) of type
	}

fun <T> Memory<T>.forget(type: Type): Memory<T> =
	itemStack.filter { !matches(type) }.memory
