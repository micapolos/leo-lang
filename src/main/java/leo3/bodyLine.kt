package leo3

data class BodyLine(val word: Word, val body: Body)

fun line(word: Word, body: Body) = BodyLine(word, body)
fun BodyLine.apply(parameter: Parameter) = line(word, body.apply(parameter))