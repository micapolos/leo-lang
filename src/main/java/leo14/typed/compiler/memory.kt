package leo14.typed.compiler

import leo.base.orNullIf
import leo13.*
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.typed.Action
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.of

data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)

sealed class MemoryItem<T>
data class EmptyMemoryItem<T>(val empty: Empty) : MemoryItem<T>()
data class RememberMemoryItem<T>(val action: Action<T>, val needsInvoke: Boolean) : MemoryItem<T>()
data class ForgetMemoryItem<T>(val type: Type) : MemoryItem<T>()

fun <T> memory(vararg items: MemoryItem<T>) =
	Memory(stack(*items))

fun <T> memoryItem(empty: Empty): MemoryItem<T> =
	EmptyMemoryItem(empty)

fun <T> remember(action: Action<T>, needsInvoke: Boolean = true): MemoryItem<T> =
	RememberMemoryItem(action, needsInvoke)

fun <T> forget(type: Type): MemoryItem<T> =
	ForgetMemoryItem(type)

fun anyMemory(): Memory<Any> =
	Memory(stack())

fun <T> Memory<T>.plus(item: MemoryItem<T>) =
	Memory(itemStack.push(item))

fun <T> Memory<T>.indexedItem(type: Type): Pair<Index, MemoryItem<T>>? =
	itemStack
		.mapFirstIndexed {
			orNullIf(!matches(type))
		}

fun <T> MemoryItem<T>.matches(type: Type): Boolean =
	when (this) {
		is EmptyMemoryItem -> false
		is RememberMemoryItem -> action.param == type
		is ForgetMemoryItem -> this.type == type
	}

fun <T> MemoryItem<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	when (this) {
		is EmptyMemoryItem -> error("Can not resolve this")
		is RememberMemoryItem ->
			if (needsInvoke) arg<T>(index).invoke(term) of action.body.type
			else arg<T>(index) of action.body.type
		is ForgetMemoryItem -> null
	}

fun <T> Memory<T>.ret(typed: Typed<T>): Typed<T> =
	typed.fold(itemStack) { ret(it) }

fun <T> Typed<T>.ret(item: MemoryItem<T>): Typed<T> =
	when (item) {
		is EmptyMemoryItem -> this
		is RememberMemoryItem -> fn(term).invoke(item.action.body.term) of type
		is ForgetMemoryItem -> this
	}
