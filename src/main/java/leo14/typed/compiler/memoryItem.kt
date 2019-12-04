package leo14.typed.compiler

import leo13.Index
import leo14.typed.Type
import leo14.typed.TypeKey
import leo14.typed.Typed
import leo14.typed.castTermTo

data class MemoryItem<T>(val key: TypeKey, val value: MemoryValue<T>)

fun <T> item(key: TypeKey, value: MemoryValue<T>) =
	MemoryItem(key, value)

fun <T> MemoryItem<T>.matches(type: Type): Boolean =
	key.type == type

fun <T> MemoryItem<T>.resolve(index: Index, typed: Typed<T>): Typed<T>? =
	typed
		.castTermTo(key.type)
		?.let { value.resolve(index, it) }
