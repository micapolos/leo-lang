package leo13.untyped.compiler

import leo13.Converter
import leo13.ObjectScripting
import leo13.Processor
import leo13.converter
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
	override val scriptingLine
		get() =
			"compiler" lineTo script(converter.scriptingLine, pattern.scriptLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		when (name) {
			"choice" -> TODO()
			else -> beginOther(name)
		}

	fun beginOther(name: String) =
		compiler(
			converter { plus(item(choice(name lineTo it))) },
			pattern())

	val end get() = converter.convert(pattern)
}

fun compiler(converter: Converter<Pattern, Token>, pattern: Pattern) =
	PatternCompiler(converter, pattern)

fun PatternCompiler.plus(item: PatternItem) =
	copy(pattern = pattern.plus(item))
