package leo25

import leo.base.Effect
import leo.base.effect
import java.io.File

data class Leo<V>(
	val run: (Environment) -> Effect<Environment, V>
)

val <T> Leo<T>.get: T
	get() =
		run(environment()).value

fun <V> leo(value: V) =
	Leo { map -> map effect value }

fun <V, O> Leo<V>.bind(fn: (V) -> Leo<O>): Leo<O> =
	Leo { map ->
		run(map).let { mapToV ->
			fn(mapToV.value).let { leoO ->
				leoO.run(mapToV.state)
			}
		}
	}

fun <V, O> Leo<V>.map(fn: (V) -> O): Leo<O> =
	bind { leo(fn(it)) }

fun leoLibrary(file: File): Leo<Library> =
	Leo { it.libraryEffect(file) }
