package leo25

import leo.base.Effect
import leo.base.effect

data class Stateful<S, out V>(
	val run: (S) -> Effect<S, V>
)

fun <S, T> Stateful<S, T>.get(state: S): T =
	run(state).value

inline fun <S, V> V.stateful() = Stateful<S, V> { map -> map effect this }

inline fun <S, V, O> Stateful<S, V>.bind(crossinline fn: (V) -> Stateful<S, O>): Stateful<S, O> =
	Stateful { map ->
		run(map).let { mapToV ->
			fn(mapToV.value).let { statefulO ->
				statefulO.run(mapToV.state)
			}
		}
	}

inline fun <S, V, O> Stateful<S, V?>.nullableBind(crossinline fn: (V) -> Stateful<S, O>): Stateful<S, O?> =
	bind {
		if (it == null) null.stateful<S, O?>()
		else fn(it)
	}

inline fun <S, V> Stateful<S, V?>.or(crossinline fn: () -> Stateful<S, V>): Stateful<S, V> =
	bind { it?.stateful() ?: fn() }

inline fun <S, V, O> Stateful<S, V>.map(crossinline fn: (V) -> O): Stateful<S, O> =
	bind { fn(it).stateful<S, O>() }

inline fun <S, V, O> Stateful<S, V?>.nullableMap(crossinline fn: (V) -> O): Stateful<S, O?> =
	nullableBind { fn(it).stateful<S, O>() }

inline fun <S, T> Stateful<S, T>.catch(crossinline fn: (Throwable) -> Stateful<S, T>): Stateful<S, T> =
	Stateful { state ->
		try {
			run(state)
		} catch (throwable: Throwable) {
			fn(throwable).run(state)
		}
	}
