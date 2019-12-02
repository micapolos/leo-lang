package leo14.typed.compiler

import leo.base.orNullIf
import leo13.*
import leo14.lambda.Term
import leo14.lambda.arg0
import leo14.typed.Type
import leo14.typed.TypeKey
import leo14.typed.Typed
import leo14.typed.of

data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)

val <T> Stack<MemoryItem<T>>.memory get() = Memory(this)

fun <T> memory(vararg items: MemoryItem<T>) =
	stack(*items).memory

fun <T> item(key: TypeKey, value: MemoryValue<T>) =
	MemoryItem(key, value)

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

fun <T> Memory<T>.resolveForEval(term: Term<T>): Term<T> =
	term.fold(itemStack) {
		resolveForEnd(it.value)
	}

fun <T> Memory<T>.resolveForEnd(term: Term<T>, index: Index): Term<T> =
	term.fold(itemStack.takeOrNull(index)!!) {
		resolveForEnd(it.value)
	}

fun <T> Memory<T>.forget(key: TypeKey): Memory<T> =
	plus(item(key, value(memoryBinding(arg0<T>() of key.type, isAction = true))))

val <T> Memory<T>.forgetEverything
	get() =
		fold(itemStack.reverse) { forget(it.key) }