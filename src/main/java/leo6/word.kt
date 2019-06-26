package leo6

data class Word(val string: String) {
	override fun toString() = string
}

fun word(string: String) = Word(string)
