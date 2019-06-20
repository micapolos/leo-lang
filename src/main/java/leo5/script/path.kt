package leo5.script

import leo.base.Empty
import leo.base.empty
import leo.base.foldRight

sealed class Path

data class EmptyPath(val empty: Empty) : Path()
data class NonEmptyPath(val nonEmpty: PathNonEmpty) : Path()

fun path(empty: Empty): Path = EmptyPath(empty)
fun path(nonEmpty: PathNonEmpty): Path = NonEmptyPath(nonEmpty)
fun path(vararg strings: String): Path = path(empty).foldRight(strings) { path(nonEmpty(this, it)) }

val Path.emptyOrNull get() = (this as? EmptyPath)?.empty
val Path.nonEmptyOrNull get() = (this as? NonEmptyPath)?.nonEmpty

