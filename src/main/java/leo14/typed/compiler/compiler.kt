package leo14.typed.compiler

import leo14.*
import leo14.native.Native
import leo14.typed.Type
import leo14.typed.typed

sealed class Compiler<T>

data class ActionParserCompiler<T>(val actionParser: ActionParser<T>) : Compiler<T>()
data class ArrowParserCompiler<T>(val arrowParser: ArrowParser<T>) : Compiler<T>()
data class ChoiceParserCompiler<T>(val choiceParser: ChoiceParser<T>) : Compiler<T>()
data class CompiledParserCompiler<T>(val compiledParser: CompiledParser<T>) : Compiler<T>()
data class DeleteParserCompiler<T>(val deleteParser: DeleteParser<T>) : Compiler<T>()
data class NativeParserCompiler<T>(val nativeParser: NativeParser<T>) : Compiler<T>()
data class NothingParserCompiler<T>(val nothingParser: NothingParser<T>) : Compiler<T>()
data class RememberParserCompiler<T>(val memoryItemParser: MemoryItemParser<T>) : Compiler<T>()
data class TypeParserCompiler<T>(val typeParser: TypeParser<T>) : Compiler<T>()
data class MatchParserCompiler<T>(val matchParser: MatchParser<T>) : Compiler<T>()
data class ScriptParserCompiler<T>(val scriptParser: ScriptParser<T>) : Compiler<T>()
data class LeonardoParserCompiler<T>(val leonardoParser: LeonardoParser<T>) : Compiler<T>()

fun <T> compiler(typeParser: TypeParser<T>): Compiler<T> = TypeParserCompiler(typeParser)
fun <T> compiler(choiceParser: ChoiceParser<T>): Compiler<T> = ChoiceParserCompiler(choiceParser)
fun <T> compiler(arrowParser: ArrowParser<T>): Compiler<T> = ArrowParserCompiler(arrowParser)
fun <T> compiler(nativeParser: NativeParser<T>): Compiler<T> = NativeParserCompiler(nativeParser)
fun <T> compiler(compiledParser: CompiledParser<T>): Compiler<T> = CompiledParserCompiler(compiledParser)
fun <T> compiler(actionParser: ActionParser<T>): Compiler<T> = ActionParserCompiler(actionParser)
fun <T> compiler(deleteParser: DeleteParser<T>): Compiler<T> = DeleteParserCompiler(deleteParser)
fun <T> compiler(nothingParser: NothingParser<T>): Compiler<T> = NothingParserCompiler(nothingParser)
fun <T> compiler(memoryItemParser: MemoryItemParser<T>): Compiler<T> = RememberParserCompiler(memoryItemParser)
fun <T> compiler(matchParser: MatchParser<T>): Compiler<T> = MatchParserCompiler(matchParser)
fun <T> compiler(scriptParser: ScriptParser<T>): Compiler<T> = ScriptParserCompiler(scriptParser)
fun <T> compiler(leonardoParser: LeonardoParser<T>): Compiler<T> = LeonardoParserCompiler(leonardoParser)

fun compiler(type: Type): Compiler<Native> =
	compiler(TypeParser(null, null, englishDictionary, type))

fun compiler(compiled: Compiled<Native>, phase: Phase = Phase.COMPILER): Compiler<Native> =
	compiler(CompiledParser(null, nativeContext, phase, compiled))

val emptyCompiler: Compiler<Native> = compiler(compiled(typed()), Phase.EVALUATOR)

fun <T> Compiler<T>.parse(token: Token): Compiler<T> =
	parseStatic(token) ?: parseDynamic(token)

// This should be moved out of this Leo class to some "preprocessor" class with "dictionary".
fun <T> Compiler<T>.parseStatic(token: Token): Compiler<T>? =
	when (token) {
		is LiteralToken -> null
		is BeginToken ->
			when (token.begin.string) {
				"comment" -> compiler(ScriptParser(CommentScriptParserParent(this), script()))
				else -> null
			}
		is EndToken -> null
	}

fun <T> Compiler<T>.parseDynamic(token: Token): Compiler<T> =
	when (this) {
		is TypeParserCompiler -> typeParser.parse(token)
		is ChoiceParserCompiler -> choiceParser.parse(token)
		is ArrowParserCompiler -> arrowParser.parse(token)
		is NativeParserCompiler -> nativeParser.parse(token)
		is CompiledParserCompiler -> compiledParser.parse(token)
		is ActionParserCompiler -> actionParser.parse(token)
		is DeleteParserCompiler -> deleteParser.parse(token)
		is NothingParserCompiler -> nothingParser.parse(token)
		is RememberParserCompiler -> memoryItemParser.parse(token)
		is MatchParserCompiler -> matchParser.parse(token)
		is ScriptParserCompiler -> scriptParser.parse(token)
		is LeonardoParserCompiler -> leonardoParser.parse(token)
	}

fun <T> Compiler<T>.parse(script: Script): Compiler<T> =
	when (script) {
		is UnitScript -> this
		is LinkScript -> parse(script.link)
	}

fun <T> Compiler<T>.parse(link: ScriptLink): Compiler<T> =
	parse(link.lhs).parse(link.line)

fun <T> Compiler<T>.parse(line: ScriptLine): Compiler<T> =
	when (line) {
		is LiteralScriptLine -> parse(token(line.literal))
		is FieldScriptLine -> parse(line.field)
	}

fun <T> Compiler<T>.parse(field: ScriptField): Compiler<T> =
	this
		.parse(token(begin(field.string)))
		.parse(field.rhs)
		.parse(token(end))
