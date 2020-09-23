package leo.base

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class Effect<out S, out V>(val state: S, val value: V)

infix fun <S, V> S.effect(value: V) = Effect(this, value)

inline fun <S, V1, V2> Effect<S, V1>.apply(fn: S.(V1) -> V2): V2 =
	state.fn(value)

infix fun <S, V1, V2> Effect<S, V1>.update(fn: (V1) -> V2): Effect<S, V2> = set(fn(value))

inline fun <S, V1, V2> Effect<S, V1>.bind(fn: S.(V1) -> Effect<S, V2>): Effect<S, V2> =
	state.fn(value)

inline fun <S, V1 : Any, V2 : Any> Effect<S, V1?>.nullableBind(fn: S.(V1) -> Effect<S, V2?>): Effect<S, V2?> =
	if (value == null) state.effect(null)
	else state.fn(value)

inline fun <S, V> Effect<S, V>.updateState(fn: S.() -> S): Effect<S, V> =
	state.fn() effect value

infix fun <S, V1, V2> Effect<S, V1>.set(value: V2): Effect<S, V2> = state effect value

fun <S, V1, V2> Effect<S, PersistentList<V1>>.persistentListMap(fn: S.(V1) -> Effect<S, V2>): Effect<S, PersistentList<V2>> =
	value.fold(state effect persistentListOf()) { mappedListEffect, item ->
		mappedListEffect.apply { mappedList ->
			fn(item).apply { mappedItem ->
				this effect mappedList.add(mappedItem)
			}
		}
	}

