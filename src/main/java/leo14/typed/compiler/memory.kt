package leo14.typed.compiler

import leo13.*
import leo14.lambda.Term
import leo14.typed.Typed

data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)

val <T> Stack<MemoryItem<T>>.memory get() = Memory(this)

fun <T> memory(vararg items: MemoryItem<T>) =
	stack(*items).memory

fun <T> Memory<T>.plus(item: MemoryItem<T>) =
	Memory(itemStack.push(item))

fun <T> Memory<T>.resolve(typed: Typed<T>): Typed<T>? =
	itemStack.indexedMapFirst { index, item ->
		item.resolve(index, typed)
	}

fun <T> Memory<T>.resolveForEval(term: Term<T>): Term<T> =
	term.fold(itemStack) {
		resolveForEnd(it.value)
	}

fun <T> Memory<T>.resolveForEnd(term: Term<T>, index: Index): Term<T> =
	term.fold(itemStack.takeOrNull(index)!!) {
		resolveForEnd(it.value)
	}
