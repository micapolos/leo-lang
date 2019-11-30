package leo14.typed.compiler

import leo.base.orIfNull
import leo13.fold
import leo13.isEmpty
import leo14.*
import leo14.parser.reflectScriptLine
import leo14.typed.reflectScriptLine
import leo14.typed.scriptLine

val <T> Compiled<T>.reflectScriptLine get() =
	"compiled" lineTo script(
		memory.reflectScriptLine,
		typed.reflectScriptLine)

val <T> Memory<T>.reflectScriptLine get() =
	"memory" lineTo
		if (itemStack.isEmpty) script("empty")
		else script().fold(itemStack) { plus(it.reflectScriptLine) }

val <T> MemoryItem<T>.reflectScriptLine get() =
	"item" lineTo script(
		when (this) {
			is EmptyMemoryItem -> "empty" lineTo script()
			is RememberMemoryItem -> "remember" lineTo script(
				function.reflectScriptLine,
				"kind" lineTo script(if (needsInvoke) "action" else "value"))
		}
	)

val Phase.reflectStringLine get() =
	"phase" lineTo script(
		when (this) {
			Phase.COMPILER -> "compiler"
			Phase.EVALUATOR -> "evaluator"
		}
	)

val <T> CompiledParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine.orIfNull { "parent" lineTo script("nothing") },
		context.reflectScriptLine,
		phase.reflectStringLine,
		compiled.reflectScriptLine)

val <T> CompiledParserParent<T>.reflectScriptLine: ScriptLine get() =
	when (this) {
		is FieldCompiledParserParent -> "begin" lineTo script(compiledParser.reflectScriptLine)
		is FunctionDoesParserParent -> null
		is FunctionGiveParserParent -> null
		is GiveCompiledParserParent -> null
		is UseCompiledParserParent -> null
		is RememberDoesParserParent -> null
		is RememberIsParserParent -> null
		is MatchParserParent -> null
	} ?: line(literal(toString()))

val <T> TypeParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine ?: "parent".line,
		type.scriptLine)

val <T> TypeParserParent<T>.reflectScriptLine: ScriptLine get() =
	"parent" lineTo script(
		when (this) {
			is LineTypeParserParent -> "begin" lineTo script(typeParser.reflectScriptLine)
			is ArrowGivingTypeParserParent -> null
			is OptionTypeParserParent -> null
			is AsTypeParserParent -> null
			is ForgetTypeParserParent -> null
		} ?: line(literal(toString()))
	)

val <T> Compiler<T>.reflectScriptLine: ScriptLine get() =
	"token" lineTo script(
		"compiler" lineTo script(
				when (this) {
					is ActionParserCompiler -> null
					is ArrowParserCompiler -> null
					is ChoiceParserCompiler -> null
					is CompiledParserCompiler -> compiledParser.reflectScriptLine
					is DeleteParserCompiler -> null
					is NothingParserCompiler -> null
					is RememberParserCompiler -> null
					is TypeParserCompiler -> typeParser.reflectScriptLine
					is MatchParserCompiler -> null
					is ScriptParserCompiler -> null
					is LeonardoParserCompiler -> null
					is ForgetEverythingParserCompiler -> null
					is ForgetEverythingEndParserCompiler -> null
				} ?: line(literal(toString()))))

val <T> CharCompiler<T>.reflectScriptLine get() =
	"char" lineTo script(
		"compiler" lineTo script(
			compiler.reflectScriptLine,
			tokenParser.reflectScriptLine))
