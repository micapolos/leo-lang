package leo13.js

data class ResultCompiler<T>(val result: T) : Compiler {
	override fun write(token: Token) = error("eof")
}

fun <T> finishCompilation(): (T) -> Compiler = { ResultCompiler(it) }

fun <T> Compiler.result(): T =
	(this as ResultCompiler<T>).result