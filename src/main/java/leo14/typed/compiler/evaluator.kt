package leo14.typed.compiler

import leo14.Token

data class Evaluator<T>(
	val compiler: Compiler<T>)

fun <T> Evaluator<T>.parse(token: Token): Evaluator<T> =
	compiler.parse(token).evaluator

val <T> Compiler<T>.evaluator: Evaluator<T>
	get() =
		when (this) {
			is ActionParserCompiler -> TODO()
			is ArrowParserCompiler -> TODO()
			is ChoiceParserCompiler -> TODO()
			is CompiledParserCompiler -> TODO()
			is DeleteParserCompiler -> TODO()
			is NativeParserCompiler -> TODO()
			is NothingParserCompiler -> TODO()
			is RememberParserCompiler -> TODO()
			is TypeParserCompiler -> TODO()
			is MatchParserCompiler -> TODO()
			is ScriptParserCompiler -> TODO()
		}

