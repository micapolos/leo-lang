package leo14.typed.compiler

import leo14.*

sealed class Compiler<T> {
	override fun toString() = "$reflectScriptLine"
}

data class ActionParserCompiler<T>(val functionParser: FunctionParser<T>) : Compiler<T>() {
	override fun toString() = super.toString()
}
data class ArrowParserCompiler<T>(val arrowParser: ArrowParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class ChoiceParserCompiler<T>(val choiceParser: ChoiceParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class CompiledParserCompiler<T>(val compiledParser: CompiledParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class NothingParserCompiler<T>(val nothingParser: NothingParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class DefineParserCompiler<T>(val defineParser: DefineParser<T>) : Compiler<T>() {
	override fun toString() = super.toString()
}
data class TypeParserCompiler<T>(val typeParser: TypeParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class MatchParserCompiler<T>(val matchParser: MatchParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class QuoteParserCompiler<T>(val quoteParser: QuoteParser<T>) : Compiler<T>() {
	override fun toString() = super.toString()
}
data class LeonardoParserCompiler<T>(val leonardoParser: LeonardoParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class ForgetEverythingParserCompiler<T>(val forgetEverythingParser: ForgetEverythingParser<T>) : Compiler<T>() { override fun toString() = super.toString() }
data class ForgetEverythingEndParserCompiler<T>(val forgetEverythingEndParser: ForgetEverythingEndParser<T>) : Compiler<T>() { override fun toString() = super.toString() }

fun <T> compiler(typeParser: TypeParser<T>): Compiler<T> = TypeParserCompiler(typeParser)
fun <T> compiler(choiceParser: ChoiceParser<T>): Compiler<T> = ChoiceParserCompiler(choiceParser)
fun <T> compiler(arrowParser: ArrowParser<T>): Compiler<T> = ArrowParserCompiler(arrowParser)
fun <T> compiler(compiledParser: CompiledParser<T>): Compiler<T> = CompiledParserCompiler(compiledParser)
fun <T> compiler(functionParser: FunctionParser<T>): Compiler<T> = ActionParserCompiler(functionParser)
fun <T> compiler(nothingParser: NothingParser<T>): Compiler<T> = NothingParserCompiler(nothingParser)
fun <T> compiler(defineParser: DefineParser<T>): Compiler<T> = DefineParserCompiler(defineParser)
fun <T> compiler(matchParser: MatchParser<T>): Compiler<T> = MatchParserCompiler(matchParser)
fun <T> compiler(quoteParser: QuoteParser<T>): Compiler<T> = QuoteParserCompiler(quoteParser)
fun <T> compiler(leonardoParser: LeonardoParser<T>): Compiler<T> = LeonardoParserCompiler(leonardoParser)

fun <T> Compiler<T>.parse(token: Token): Compiler<T> =
	parseStatic(token) ?: parseDynamic(token)

// This should be moved out of this Leo class to some "preprocessor" class with "dictionary".
fun <T> Compiler<T>.parseStatic(token: Token): Compiler<T>? =
	when (token) {
		is LiteralToken -> null
		is BeginToken ->
			when (token.begin.string) {
				"comment" -> compiler(QuoteParser(CommentQuoteParserParent(this), script()))
				else -> null
			}
		is EndToken -> null
	}

fun <T> Compiler<T>.parseDynamic(token: Token): Compiler<T> =
	when (this) {
		is TypeParserCompiler -> typeParser.parse(token)
		is ChoiceParserCompiler -> choiceParser.parse(token)
		is ArrowParserCompiler -> arrowParser.parse(token)
		is CompiledParserCompiler -> compiledParser.parse(token)
		is ActionParserCompiler -> functionParser.parse(token)
		is NothingParserCompiler -> nothingParser.parse(token)
		is DefineParserCompiler -> defineParser.parse(token)
		is MatchParserCompiler -> matchParser.parse(token)
		is QuoteParserCompiler -> quoteParser.parse(token)
		is LeonardoParserCompiler -> leonardoParser.parse(token)
		is ForgetEverythingParserCompiler -> forgetEverythingParser.parse(token)
		is ForgetEverythingEndParserCompiler -> forgetEverythingEndParser.parse(token)
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
