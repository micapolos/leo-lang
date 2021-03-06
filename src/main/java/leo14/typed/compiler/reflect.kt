package leo14.typed.compiler

import leo.base.orIfNull
import leo14.*
import leo14.parser.reflectScriptLine
import leo14.reader.CharReader
import leo14.reader.reflectScriptLine
import leo14.typed.reflectScriptLine
import leo14.typed.script
import leo14.typed.scriptLine

fun <T> Compiled<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine): ScriptLine =
	"compiled" lineTo script(
		memory.reflectScriptLine(nativeFn),
		typed.reflectScriptLine(nativeFn),
		"local" lineTo script(
			"memory" lineTo script(
				"size" lineTo script(literal(localMemorySize)))))

fun <T> Memory<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
	itemStack.reflectOrEmptyScriptLine("memory") { reflectScriptLine(nativeFn) }

fun <T> MemoryItem<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
	"item" lineTo script(
		key.reflectScriptLine,
		value.reflectScriptLine(nativeFn))

fun <T> MemoryValue<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
		"value" lineTo script(
			when (this) {
				is ArgumentMemoryValue -> "argument" lineTo type.script
				is BindingMemoryValue -> binding.reflectScriptLine(nativeFn)
			})

fun <T> MemoryBinding<T>.reflectScriptLine(nativeFn: T.() -> ScriptLine) =
		"binding" lineTo script(
			"kind" lineTo script(if (isAction) "action" else "constant"),
			typed.reflectScriptLine(nativeFn))

val <T> CompiledParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine.orIfNull { "parent" lineTo script("nothing") },
		context.reflectScriptLine,
		kind.reflectScriptLine,
		compiled.reflectScriptLine(context.nativeScriptLine))

val <T> CompiledParserParent<T>.reflectScriptLine: ScriptLine get() =
	when (this) {
		is FieldCompiledParserParent -> "begin" lineTo script(compiledParser.reflectScriptLine)
		is FunctionDoesParserParent -> null
		is UseCompiledParserParent -> null
		is DefineGivesParserParent -> null
		is DefineIsParserParent -> null
		is MatchParserParent -> null
	} ?: line(literal(toString()))

val <T> FunctionParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parentCompiledParser.reflectScriptLine,
		function.reflectScriptLine(parentCompiledParser.context.nativeScriptLine))

val <T> TypeParser<T>.reflectScriptLine: ScriptLine get() =
	"parser" lineTo script(
		parent?.reflectScriptLine ?: "parent".line,
		type.scriptLine)

val <T> TypeParserParent<T>.reflectScriptLine: ScriptLine get() =
	"parent" lineTo script(
		when (this) {
			is LineTypeParserParent -> "begin" lineTo script(typeParser.reflectScriptLine)
			is CompiledTypeParserParent -> "begin" lineTo script(compiledParser.reflectScriptLine)
			is ArrowGivesTypeParserParent -> null
			is OptionTypeParserParent -> null
			is AsTypeParserParent -> null
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
					is DefineParserCompiler -> defineParser.reflectScriptLine
					is TypeParserCompiler -> typeParser.reflectScriptLine
					is MatchParserCompiler -> matchParser.reflectScriptLine
					is QuoteParserCompiler -> quoteParser.reflectScriptLine
				} ?: line(literal(toString()))))

val CharReader.reflectScriptLine
	get() =
	"char" lineTo script(
		"compiler" lineTo script(
			tokenReader.reflectScriptLine,
			tokenParser.reflectScriptLine))

val <T> DefineParser<T>.reflectScriptLine
	get() =
		"define" lineTo script(
			"parser" lineTo script(
				"parent" lineTo script(parentCompiledParser.reflectScriptLine),
				memory.reflectScriptLine(parentCompiledParser.context.nativeScriptLine)))

val <T> QuoteParser<T>.reflectScriptLine get() =
	"quote" lineTo script(
		"parser" lineTo script(
			parent.reflectScriptLine,
			"quoted" lineTo script))

val <T> QuoteParserParent<T>.reflectScriptLine: ScriptLine get() =
	"parent" lineTo
		when (this) {
			is FieldQuoteParserParent ->
				script(
					quoteParser.reflectScriptLine,
					"name" lineTo script(name))
			is MakeQuoteParserParent ->
				script(compiledParser.reflectScriptLine)
			is CommentQuoteParserParent ->
				script(compiler.reflectScriptLine)
			is CompiledQuoteParserParent ->
				script(compiledParser.reflectScriptLine)
		}