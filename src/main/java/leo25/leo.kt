package leo25

import leo.base.Effect
import leo.base.effect
import java.io.File

data class Leo<out V>(
	val run: (Environment) -> Effect<Environment, V>
)

val <T> Leo<T>.get: T
	get() =
		run(environment()).value

fun <V> leo(value: V) =
	Leo { map -> map effect value }

val <V> V.leo get() = Leo { map -> map effect this }

fun <V, O> Leo<V>.bind(fn: (V) -> Leo<O>): Leo<O> =
	Leo { map ->
		run(map).let { mapToV ->
			fn(mapToV.value).let { leoO ->
				leoO.run(mapToV.state)
			}
		}
	}

fun <V, O> Leo<V?>.nullableBind(fn: (V) -> Leo<O>): Leo<O?> =
	bind {
		if (it == null) leo<O?>(null)
		else fn(it)
	}

fun <V> Leo<V?>.or(fn: () -> Leo<V>): Leo<V> =
	bind { it?.leo ?: fn() }

fun <V, O> Leo<V>.map(fn: (V) -> O): Leo<O> =
	bind { fn(it).leo }

fun <V, O> Leo<V?>.nullableMap(fn: (V) -> O): Leo<O?> =
	nullableBind { leo(fn(it)) }

fun <T> Leo<T>.catch(fn: (Throwable) -> Leo<T>): Leo<T> =
	Leo { environment ->
		try {
			run(environment)
		} catch (throwable: Throwable) {
			fn(throwable).run(environment)
		}
	}
