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
	val arrows: PatternArrows,
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
			"choice" -> choiceCompiler(converter { plus(item(it)) }, arrows)
			else -> beginOther(name)
		}

	fun beginOther(name: String): Processor<Token> =
		patternCompiler(
			converter { plus(item(choice(name lineTo it))) },
			arrows,
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)
}

fun patternCompiler(
	converter: Converter<Pattern, Token> = errorConverter(),
	arrows: PatternArrows = patternArrows(),
	pattern: Pattern = pattern()) =
	PatternCompiler(converter, arrows, pattern)

fun PatternCompiler.plus(item: PatternItem) =
	set(
		pattern
			.plus(item)
			.let { arrows.resolve(it) }
	)

fun PatternCompiler.set(pattern: Pattern) =
	copy(pattern = pattern)
