package leo14.typed.compiler

import leo14.typed.coreString

val Compiler<*>.promptString: String
	get() =
		when (this) {
			is ActionParserCompiler -> "action"
			is ArrowParserCompiler -> "arrow"
			is ChoiceParserCompiler -> "option"
			is CompiledParserCompiler -> compiledParser.promptString
			is DeleteParserCompiler -> deleteParser.parentCompiledParser.promptString
			is NativeParserCompiler -> "type"
			is NothingParserCompiler -> nothingParser.parentCompiledParser.promptString
			is RememberParserCompiler -> "remember"
			is TypeParserCompiler -> "type"
			is MatchParserCompiler -> "match"
			is ScriptParserCompiler -> "script"
		}

val CompiledParser<*>.promptString
	get() =
		when (phase) {
			Phase.COMPILER -> "compiler"
			Phase.EVALUATOR -> "${compiled.typed.type.coreString}"
		}
