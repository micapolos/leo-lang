package leo6

data class Begin(val word: Word) {
	override fun toString() = "$word("
}

fun begin(word: Word) = Begin(word)
