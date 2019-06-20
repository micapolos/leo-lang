package leo5.value

data class PathLink(val path: Path, val word: Word)
infix fun Path.linkTo(word: Word) = PathLink(this, word)
