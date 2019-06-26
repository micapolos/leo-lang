package leo6

import leo.base.foldRight

sealed class Path

data class NothingPath(val nothing: Nothing) : Path() {
	override fun toString() = "$nothing"
}

data class LinkPath(val link: PathLink) : Path() {
	override fun toString() = "$link"
}

fun path(): Path = NothingPath(nothing)
infix fun Word.pathTo(path: Path) = LinkPath(this linkTo path)
infix fun String.pathTo(path: Path) = word(this) pathTo path
fun path(vararg words: String) = path().foldRight(words) { pathTo(it) }

val Path.nothingOrNull get() = (this as? NothingPath)?.nothing

fun Path.startsWith(path: Path): Boolean = when (this) {
	is NothingPath -> when (path) {
		is NothingPath -> true
		is LinkPath -> false
	}
	is LinkPath -> when (path) {
		is NothingPath -> true
		is LinkPath -> link.startsWith(path.link)
	}
}

fun PathLink.startsWith(link: PathLink) =
	firstWord == link.firstWord && remainingPath.startsWith(link.remainingPath)
