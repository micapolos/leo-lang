package leo6

sealed class Parent

data class NothingParent(val nothing: Nothing) : Parent() {
	override fun toString() = "$nothing"
}

data class LinkParent(val link: ParentLink) : Parent() {
	override fun toString() = "$link"
}

fun parent(): Parent = NothingParent(nothing)
fun parent(link: ParentLink): Parent = LinkParent(link)

val Parent.linkOrNull get() = (this as? LinkParent)?.link

fun Parent.cursorAt(word: Word) = linkOrNull?.cursorAt(word)
fun Parent.cursorAt(path: Path) = linkOrNull?.cursorAt(path)

fun Parent.rootPathTo(path: Path) = when (this) {
	is NothingParent -> path.nothingOrNull?.run { path }
	is LinkParent -> link.cursor.rootPathTo(path)
}

fun Parent.recursiveRootPathTo(path: Path, acc: Path) = when (this) {
	is NothingParent -> null
	is LinkParent -> link.recursiveRootPathTo(path, acc)
}
