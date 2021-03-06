package leo14.typed.compiler

import leo14.typed.*

sealed class CompiledParserParent<T>

data class FieldCompiledParserParent<T>(val compiledParser: CompiledParser<T>, val name: String) : CompiledParserParent<T>()
data class FunctionDoesParserParent<T>(val compiledParser: CompiledParser<T>, val type: Type) : CompiledParserParent<T>()
data class UseCompiledParserParent<T>(val compiledParser: CompiledParser<T>) : CompiledParserParent<T>()
data class DefineGivesParserParent<T>(val defineParser: DefineParser<T>, val type: Type) : CompiledParserParent<T>()
data class DefineIsParserParent<T>(val defineParser: DefineParser<T>, val type: Type) : CompiledParserParent<T>()
data class MatchParserParent<T>(val matchParser: MatchParser<T>, val name: String) : CompiledParserParent<T>()

fun <T> CompiledParserParent<T>.end(typed: Typed<T>): Compiler<T> =
	when (this) {
		is FieldCompiledParserParent ->
			compiledParser.resolveCompiler(line(name fieldTo typed))
		is FunctionDoesParserParent ->
			compiler(FunctionParser(compiledParser, type does typed))
		is UseCompiledParserParent ->
			compiledParser.nextCompiler { updateTyped { typed } }
		is DefineGivesParserParent ->
			compiler(defineParser.plus(item(key(type), value(memoryBinding(typed, isAction = true)))))
		is DefineIsParserParent ->
			compiler(defineParser.plus(item(key(type), value(memoryBinding(typed, isAction = false)))))
		is MatchParserParent ->
			compiler(matchParser.plus(name, typed))
	}