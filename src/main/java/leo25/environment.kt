package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.effect
import leo.base.orIfNull
import leo14.literal
import java.io.File
import java.io.IOException

data class Environment(
	val fileLibraryMap: PersistentMap<File, Resolver>,
	val traceOrNull: Trace?
)

fun environment() = Environment(persistentMapOf(), emptyTrace)

fun Environment.libraryEffect(file: File): Effect<Environment, Resolver> =
	fileLibraryMap[file]
		?.let { this effect it }
		?: loadLibrary(file).let { library ->
			copy(fileLibraryMap = fileLibraryMap.put(file, library)) effect library
		}

val Value.tracedLeo: Leo<Unit>
	get() =
		Leo { it.copy(traceOrNull = it.traceOrNull?.push(this)) effect Unit }

fun loadLibrary(file: File): Resolver =
	try {
		file.readText().resolver
	} catch (ioException: IOException) {
		value(field(literal(ioException.message ?: ioException.toString()))).throwError()
	}

val traceValueLeo: Leo<Value>
	get() =
		Leo { it effect it.traceOrNull?.value.orIfNull { value(traceName fieldTo value("unavailable")) } }