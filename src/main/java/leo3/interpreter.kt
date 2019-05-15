package leo3

data class Interpreter(
	val parentOrNull: Interpreter?,
	val value: Value)

fun Interpreter.begin(word: Word): Interpreter = TODO()
fun Interpreter.end(): Interpreter? = TODO()