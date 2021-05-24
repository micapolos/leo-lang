package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.effect
import leo.base.orIfNull
import leo.java.io.file
import leo14.literal
import java.io.IOException

data class Environment(
	val fileLibraryMap: PersistentMap<Use, Resolver>,
	val traceOrNull: Trace?
)

fun environment() = Environment(persistentMapOf(), emptyTrace)

fun Environment.libraryEffect(use: Use): Effect<Environment, Resolver> =
	fileLibraryMap[use]
		?.let { this effect it }
		?: loadLibrary(use).let { library ->
			copy(fileLibraryMap = fileLibraryMap.put(use, library)) effect library
		}

val Value.tracedLeo: Leo<Unit>
	get() =
		Leo { it.copy(traceOrNull = it.traceOrNull?.push(this)) effect Unit }

fun loadLibrary(use: Use): Resolver =
	try {
		use.path.file.readText().resolver
	} catch (ioException: IOException) {
		value(field(literal(ioException.message ?: ioException.toString()))).throwError()
	}

val traceValueLeo: Leo<Value>
	get() =
		Leo { it effect it.traceOrNull?.value.orIfNull { value(traceName fieldTo value("unavailable")) } }
