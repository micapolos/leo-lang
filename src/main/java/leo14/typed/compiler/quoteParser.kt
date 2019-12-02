package leo14.typed.compiler

import leo14.*

data class QuoteParser<T>(
	val parent: QuoteParserParent<T>,
	val script: Script)

sealed class QuoteParserParent<T>

data class FieldQuoteParserParent<T>(val quoteParser: QuoteParser<T>, val name: String) : QuoteParserParent<T>()
data class MakeQuoteParserParent<T>(val compiledParser: CompiledParser<T>) : QuoteParserParent<T>()
data class CommentQuoteParserParent<T>(val compiler: Compiler<T>) : QuoteParserParent<T>()
data class CompiledQuoteParserParent<T>(val compiledParser: CompiledParser<T>) : QuoteParserParent<T>()

fun <T> QuoteParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> compiler(plus(line(token.literal)))
		is BeginToken -> compiler(QuoteParser(FieldQuoteParserParent(this, token.begin.string), script()))
		is EndToken -> parent.end(script)
	}

fun <T> QuoteParserParent<T>.end(script: Script): Compiler<T> =
	when (this) {
		is FieldQuoteParserParent -> compiler(quoteParser.plus(name lineTo script))
		is MakeQuoteParserParent -> compiledParser.make(script)
		is CommentQuoteParserParent -> compiler
		is CompiledQuoteParserParent -> compiler(compiledParser.plus(script))
	}

fun <T> QuoteParser<T>.plus(line: ScriptLine) =
	copy(script = script.plus(line))
