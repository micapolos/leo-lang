package leo25

import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import leo.base.Effect
import java.io.File

data class Environment(
	val fileLibraryMap: PersistentMap<File, Resolver>
)

fun environment() = Environment(persistentMapOf())

fun Environment.libraryEffect(file: File): Effect<Environment, Resolver> =
	TODO()