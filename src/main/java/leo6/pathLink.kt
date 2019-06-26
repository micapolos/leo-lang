package leo6

data class PathLink(val firstWord: Word, val remainingPath: Path) {
	override fun toString() = ".$firstWord$remainingPath"
}

infix fun Word.linkTo(path: Path) = PathLink(this, path)
