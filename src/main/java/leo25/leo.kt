package leo25

import leo.base.Effect
import leo.base.effect

data class Leo<out V>(
	val run: (Environment) -> Effect<Environment, V>
)

val <T> Leo<T>.get: T
	get() =
		run(environment()).value

inline fun <V> leo(value: V) =
	Leo { it effect value }

inline val <V> V.leo get() = Leo { map -> map effect this }

inline fun <V, O> Leo<V>.bind(crossinline fn: (V) -> Leo<O>): Leo<O> =
	Leo { map ->
		run(map).let { mapToV ->
			fn(mapToV.value).let { leoO ->
				leoO.run(mapToV.state)
			}
		}
	}

inline fun <V, O> Leo<V?>.nullableBind(crossinline fn: (V) -> Leo<O>): Leo<O?> =
	bind {
		if (it == null) leo<O?>(null)
		else fn(it)
	}

inline fun <V> Leo<V?>.or(crossinline fn: () -> Leo<V>): Leo<V> =
	bind { it?.leo ?: fn() }

inline fun <V, O> Leo<V>.map(crossinline fn: (V) -> O): Leo<O> =
	bind { fn(it).leo }

inline fun <V, O> Leo<V?>.nullableMap(crossinline fn: (V) -> O): Leo<O?> =
	nullableBind { leo(fn(it)) }

inline fun <T> Leo<T>.catch(crossinline fn: (Throwable) -> Leo<T>): Leo<T> =
	Leo { environment ->
		try {
			run(environment)
		} catch (throwable: Throwable) {
			fn(throwable).run(environment)
		}
	}
