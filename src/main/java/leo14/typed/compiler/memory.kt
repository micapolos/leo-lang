package leo14.typed.compiler

import leo.base.orNullIf
import leo13.*
import leo14.lambda.*
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.of

data class Memory<T>(val itemStack: Stack<MemoryItem<T>>)

data class MemoryItem<T>(val key: TypeKey, val value: MemoryValue<T>)

data class TypeKey(val type: Type)
sealed class MemoryValue<T>
data class ArgumentMemoryValue<T>(val type: Type) : MemoryValue<T>()
data class BindingMemoryValue<T>(val binding: MemoryBinding<T>) : MemoryValue<T>()

data class MemoryBinding<T>(val typed: Typed<T>, val isAction: Boolean)

fun <T> argumentMemoryValue(type: Type): MemoryValue<T> = ArgumentMemoryValue(type)
fun <T> value(binding: MemoryBinding<T>): MemoryValue<T> = BindingMemoryValue(binding)

fun <T> memoryBinding(typed: Typed<T>, isAction: Boolean) = MemoryBinding(typed, isAction)
fun key(type: Type) = TypeKey(type)

fun <T> MemoryValue<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	arg<T>(index)
		.let { indexTerm ->
			when (this) {
				is ArgumentMemoryValue -> indexTerm of type
				is BindingMemoryValue ->
					if (binding.isAction) indexTerm.invoke(term) of binding.typed.type
					else indexTerm of binding.typed.type
			}
		}

val <T> MemoryValue<T>.type
	get() =
		when (this) {
			is ArgumentMemoryValue -> type
			is BindingMemoryValue -> binding.typed.type
		}

fun <T> Term<T>.resolveForEnd(value: MemoryValue<T>): Term<T> =
	when (value) {
		is ArgumentMemoryValue -> fn(this)
		is BindingMemoryValue -> fn(this).invoke(value.binding.typed.term)
	}

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

fun <T> MemoryItem<T>.matches(type: Type): Boolean =
	key.type == type

fun <T> MemoryItem<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	value.resolve(index, term)

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