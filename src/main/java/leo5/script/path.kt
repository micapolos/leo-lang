package leo5.script

import leo.base.Empty

sealed class Path

data class EmptyPath(val empty: Empty) : Path()
data class NonEmptyPath(val nonEmpty: PathNonEmpty) : Path()

fun path(empty: Empty): Path = EmptyPath(empty)
fun path(nonEmpty: PathNonEmpty): Path = NonEmptyPath(nonEmpty)

val Path.emptyOrNull get() = (this as? EmptyPath)?.empty
val Path.nonEmptyOrNull get() = (this as? NonEmptyPath)?.nonEmpty

operator fun Path.plus(string: String) = path(nonEmpty(this, string))
