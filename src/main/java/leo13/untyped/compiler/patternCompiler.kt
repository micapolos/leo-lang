package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.pattern.*

data class PatternCompiler(
	val converter: Converter<Pattern, Token>,
	val pattern: Pattern
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"compiler" lineTo script(converter.scriptingLine, pattern.scriptLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			"choice" -> choiceCompiler(converter { plus(item(it)) })
			else -> beginOther(name)
		}

	fun beginOther(name: String): Processor<Token> =
		patternCompiler(
			converter { plus(item(choice(name lineTo it))) },
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)
}

fun patternCompiler(
	converter: Converter<Pattern, Token> = errorConverter(),
	pattern: Pattern = pattern()) =
	PatternCompiler(converter, pattern)

fun PatternCompiler.plus(item: PatternItem) =
	set(pattern.plus(item))

fun PatternCompiler.set(pattern: Pattern) =
	copy(pattern = pattern)
