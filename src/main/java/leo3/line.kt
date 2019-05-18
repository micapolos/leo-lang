package leo3

data class Line(
	val word: Word,
	val value: Value)

fun line(word: Word, value: Value = value()) =
	Line(word, value)

fun line(string: String, value: Value = value()) =
	Line(word(string), value)
