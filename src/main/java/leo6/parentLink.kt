package leo6

data class ParentLink(val cursor: Cursor, val word: Word) {
	override fun toString() = "$cursor$word("
}

infix fun Cursor.linkTo(word: Word) = ParentLink(this, word)

fun ParentLink.cursorAt(word: Word) = cursor.at(word)
fun ParentLink.cursorAt(path: Path) = cursor.at(path)

fun ParentLink.rootPathTo(path: Path) = cursor.rootPathTo(word pathTo path)

fun ParentLink.recursiveRootPathTo(path: Path, acc: Path) =
	cursor.recursiveRootPathTo(path, word pathTo acc)