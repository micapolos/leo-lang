package leo13.js.compiler

data class ResultCompiler<T>(val result: T) : Compiler {
	override fun write(token: Token) = error("eof")
}

fun <T> resultCompiler(result: T) = ResultCompiler(result)

fun <T> ret(): (T) -> Compiler = { resultCompiler(it) }

fun <T> Compiler.result(): T =
	(this as ResultCompiler<T>).result

