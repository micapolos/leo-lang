package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import leo.base.effect
import leo13.Stack
import leo13.stack
import java.io.File

data class Environment(
	val fileLibraryMap: PersistentMap<File, Resolver>,
	val traceStack: Stack<Value>
)

fun environment() = Environment(persistentMapOf(), stack())

fun Environment.libraryEffect(file: File): Effect<Environment, Resolver> =
	fileLibraryMap[file]
		?.let { this effect it }
		?: loadLibrary(file).let { library ->
			Environment(fileLibraryMap.put(file, library), traceStack) effect library
		}

fun loadLibrary(file: File): Resolver =
	file.readText().resolver
