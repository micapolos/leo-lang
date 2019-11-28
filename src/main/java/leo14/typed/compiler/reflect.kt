package leo14.typed.compiler

import leo.base.orIfNull
import leo13.fold
import leo14.*
import leo14.parser.reflectScriptLine
import leo14.typed.reflectScriptLine
import leo14.typed.script

val <T> Compiled<T>.reflectScriptLine get() =
	"compiled" lineTo script(
		memory.reflectScriptLine,
		typed.reflectScriptLine)

val <T> Memory<T>.reflectScriptLine get() =
	"memory" lineTo script().fold(itemStack) { plus(it.reflectScriptLine) }

val <T> MemoryItem<T>.reflectScriptLine get() =
	when (this) {
		is EmptyMemoryItem -> "empty" lineTo script()
		is RememberMemoryItem -> "remember" lineTo script(action.reflectScriptLine)
	}

val Phase.reflectStringLine get() =
	"phase" lineTo script(
		when (this) {
			Phase.COMPILER -> "compiler"
			Phase.EVALUATOR -> "evaluator"
		}
	)

val <T> CompiledParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine.orIfNull { "parent".line },
		phase.reflectStringLine,
		compiled.reflectScriptLine)

val <T> CompiledParserParent<T>.reflectScriptLine: ScriptLine get() =
	when (this) {
		is FieldCompiledParserParent -> "begin" lineTo script(compiledParser.reflectScriptLine)
		is ActionDoesParserParent -> TODO()
		is ActionDoParserParent -> TODO()
		is GiveCompiledParserParent -> TODO()
		is UseCompiledParserParent -> TODO()
		is RememberDoesParserParent -> TODO()
		is RememberIsParserParent -> TODO()
		is MatchParserParent -> TODO()
	}

val <T> TypeParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine ?: "parent".line,
		"type" lineTo type.script)

val <T> TypeParserParent<T>.reflectScriptLine: ScriptLine get() =
	"parent" lineTo script(
		when (this) {
			is LineTypeParserParent -> "begin" lineTo script(typeParser.reflectScriptLine)
			is ArrowGivingTypeParserParent -> TODO()
			is OptionTypeParserParent -> TODO()
			is AsTypeParserParent -> TODO()
			is ForgetTypeParserParent -> TODO()
		}
	)

val <T> Compiler<T>.reflectScriptLine: ScriptLine get() =
	"token" lineTo script(
		"compiler" lineTo script(
				when (this) {
					is ActionParserCompiler -> TODO()
					is ArrowParserCompiler -> TODO()
					is ChoiceParserCompiler -> TODO()
					is CompiledParserCompiler -> compiledParser.reflectScriptLine
					is DeleteParserCompiler -> TODO()
					is NativeParserCompiler -> TODO()
					is NothingParserCompiler -> TODO()
					is RememberParserCompiler -> TODO()
					is TypeParserCompiler -> typeParser.reflectScriptLine
					is MatchParserCompiler -> TODO()
					is ScriptParserCompiler -> TODO()
					is LeonardoParserCompiler -> TODO()
					is ForgetEverythingParserCompiler -> TODO()
					is ForgetEverythingEndParserCompiler -> TODO()
				}))

val <T> CharCompiler<T>.reflectScriptLine get() =
	"char" lineTo script(
		"compiler" lineTo script(
			compiler.reflectScriptLine,
			tokenParser.reflectScriptLine))
