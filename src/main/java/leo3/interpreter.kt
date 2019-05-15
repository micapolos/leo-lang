package leo3

data class Interpreter(
	val parentOrNull: Interpreter?,
	val function: Function)

fun Interpreter.invoke(token: Token): Interpreter? =
	when (token) {
		is BeginToken -> invoke(token.begin)
		is EndToken -> invoke(token.end)
	}

fun Interpreter.invoke(begin: Begin): Interpreter = TODO()
fun Interpreter.invoke(end: End): Interpreter = TODO()
