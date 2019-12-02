package leo14.typed.compiler

import leo.base.orIfNull
import leo13.int
import leo14.*
import leo14.parser.reflectScriptLine
import leo14.reader.CharReader
import leo14.reader.reflectScriptLine
import leo14.typed.reflectScriptLine
import leo14.typed.script
import leo14.typed.scriptLine

val <T> Compiled<T>.reflectScriptLine: ScriptLine get() =
	"compiled" lineTo script(
		memory.reflectScriptLine,
		typed.reflectScriptLine,
		"local" lineTo script(
			"memory" lineTo script(
				"size" lineTo script(literal(localMemorySize.int)))))

val <T> Memory<T>.reflectScriptLine get() =
	itemStack.reflectOrEmptyScriptLine("memory", MemoryItem<T>::reflectScriptLine)

val <T> MemoryItem<T>.reflectScriptLine get() =
	"item" lineTo script(
		key.reflectScriptLine,
		value.reflectScriptLine)

val <T> MemoryValue<T>.reflectScriptLine
	get() =
		"value" lineTo script(
			when (this) {
				is ArgumentMemoryValue -> "argument" lineTo type.script
				is BindingMemoryValue -> binding.reflectScriptLine
			})

val <T> MemoryBinding<T>.reflectScriptLine
	get() =
		"binding" lineTo script(
			"kind" lineTo script(if (isAction) "action" else "constant"),
			typed.reflectScriptLine)

val <T> CompiledParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine.orIfNull { "parent" lineTo script("nothing") },
		context.reflectScriptLine,
		compiled.reflectScriptLine)

val <T> CompiledParserParent<T>.reflectScriptLine: ScriptLine get() =
	when (this) {
		is FieldCompiledParserParent -> "begin" lineTo script(compiledParser.reflectScriptLine)
		is FunctionDoesParserParent -> null
		is FunctionApplyParserParent -> null
		is GiveCompiledParserParent -> null
		is UseCompiledParserParent -> null
		is DefineGivesParserParent -> null
		is DefineIsParserParent -> null
		is MatchParserParent -> null
		is ExitParserParent -> null
	} ?: line(literal(toString()))

val <T> FunctionParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parentCompiledParser.reflectScriptLine,
		function.reflectScriptLine)

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
					is ActionParserCompiler -> functionParser.reflectScriptLine
					is ArrowParserCompiler -> null
					is ChoiceParserCompiler -> null
					is CompiledParserCompiler -> compiledParser.reflectScriptLine
					is DeleteParserCompiler -> null
					is NothingParserCompiler -> null
					is RememberParserCompiler -> memoryItemParser.reflectScriptLine
					is TypeParserCompiler -> typeParser.reflectScriptLine
					is MatchParserCompiler -> null
					is ScriptParserCompiler -> null
					is LeonardoParserCompiler -> null
					is ForgetEverythingParserCompiler -> null
					is ForgetEverythingEndParserCompiler -> null
				} ?: line(literal(toString()))))

val CharReader.reflectScriptLine
	get() =
	"char" lineTo script(
		"compiler" lineTo script(
			tokenReader.reflectScriptLine,
			tokenParser.reflectScriptLine))

val <T> MemoryItemParser<T>.reflectScriptLine
	get() =
		"parser" lineTo script(
			"parent" lineTo script(parentCompiledParser.reflectScriptLine),
			memoryItem.reflectScriptLine)
