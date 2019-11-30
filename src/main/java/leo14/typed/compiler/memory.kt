package leo14.typed.compiler

import leo.base.notNullIf
import leo.base.orNullIf
import leo13.*
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.compiler.MemoryItemState.FORGOTTEN
import leo14.typed.compiler.MemoryItemState.REMEMBERED
import leo14.typed.of

enum class MemoryItemState { REMEMBERED, FORGOTTEN }
data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)
data class MemoryItem<T>(val definition: Definition<T>, val state: MemoryItemState)

val <T> Stack<MemoryItem<T>>.memory get() = Memory(this)

fun <T> memory(vararg items: MemoryItem<T>) =
	stack(*items).memory

fun <T> item(value: Definition<T>, state: MemoryItemState = REMEMBERED) =
	MemoryItem(value, state)

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
	when (state) {
		REMEMBERED -> definition.matches(type)
		FORGOTTEN -> false
	}

fun <T> MemoryItem<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	when (state) {
		REMEMBERED -> definition.resolve(index, term)
		FORGOTTEN -> null
	}

fun <T> Memory<T>.ret(typed: Typed<T>): Typed<T> =
	typed.fold(itemStack) { ret(it) }

fun <T> Typed<T>.ret(item: MemoryItem<T>): Typed<T> =
	when (item.state) {
		REMEMBERED -> fn(item.definition.function.does.term) of type
		FORGOTTEN -> this
	}

fun <T> Memory<T>.forget(type: Type): Memory<T> =
	itemStack
		.updateFirst { forgetOrNull(type) }
		?.memory
		?: this

val <T> MemoryItem<T>.forget
	get() =
		copy(state = FORGOTTEN)

fun <T> MemoryItem<T>.forgetOrNull(type: Type): MemoryItem<T>? =
	notNullIf(definition.matches(type)) { forget }

val <T> Memory<T>.forgetEverything
	get() =
		itemStack.map { forget }.memory