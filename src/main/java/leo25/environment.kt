package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.effect
import java.io.File

data class Environment(
	val fileLibraryMap: PersistentMap<File, Resolver>
)

fun environment() = Environment(persistentMapOf())

fun Environment.libraryEffect(file: File): Effect<Environment, Resolver> =
	fileLibraryMap[file]
		?.let { this effect it }
		?: loadLibrary(file).let { library ->
			Environment(fileLibraryMap.put(file, library)) effect library
		}

fun loadLibrary(file: File): Resolver =
	file.readText().resolver
