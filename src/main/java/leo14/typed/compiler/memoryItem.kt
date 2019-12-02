package leo14.typed.compiler

import leo13.Index
import leo14.lambda.Term
import leo14.typed.Type
import leo14.typed.TypeKey
import leo14.typed.Typed

data class MemoryItem<T>(val key: TypeKey, val value: MemoryValue<T>)

fun <T> MemoryItem<T>.matches(type: Type): Boolean =
	key.type == type

fun <T> MemoryItem<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	value.resolve(index, term)

