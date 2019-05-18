package leo3

data class Line(
	val word: Word,
	val value: Value)

fun line(word: Word, value: Value) =
	Line(word, value)
