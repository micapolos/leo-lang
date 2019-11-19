package leo14.typed.compiler

import leo14.typed.coreString

val Leo<*>.promptString: String
	get() =
		when (this) {
			is ActionParserLeo -> "action"
			is ArrowParserLeo -> "arrow"
			is ChoiceParserLeo -> "option"
			is CommentParserLeo -> "comment"
			is CompiledParserLeo -> compiledParser.promptString
			is DeleteParserLeo -> deleteParser.parentCompiledParser.promptString
			is NativeParserLeo -> "type"
			is NothingParserLeo -> nothingParser.parentCompiledParser.promptString
			is RememberParserLeo -> "remember"
			is TypeParserLeo -> "type"
		}

val CompiledParser<*>.promptString
	get() =
		when (phase) {
			Phase.COMPILER -> "compiler"
			Phase.EVALUATOR -> "${compiled.typed.type.coreString}"
		}
