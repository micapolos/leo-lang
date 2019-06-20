package leo5.value

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Path

data class EmptyPath(val empty: Empty): Path()
data class LinkPath(val link: PathLink): Path()

fun path(): Path = EmptyPath(empty)
operator fun Path.plus(word: Word) = LinkPath(this linkTo word)
fun path(vararg words: Word) = path().fold(words, Path::plus)
