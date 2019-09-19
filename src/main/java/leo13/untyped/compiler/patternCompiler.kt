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
	val partial: Boolean,
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
			// TODO: There shouid be slightly different path for "either" and "or":
			// - "either" should be allowed only at the beginning of pattern, and after another "either"
			// - "or" should be allowed only after first pattern line, and after another "or"
			"either" -> beginEither
			"or" -> beginEither
			else -> beginOther(name)
		}

	val beginEither: Processor<Token>
		get() =
			pattern
				.linkOrNull
				?.let { link ->
					link
						.item
						.choiceOrNull
						?.let { choice ->
							eitherCompiler(
								converter { either ->
									copy(pattern = link.lhs).plus(item(choice.plus(either)))
								},
								arrows)
						}
						?: tracedError("not" lineTo script("expected" lineTo script("either")))
				}
				?: eitherCompiler(
					converter { either ->
						plus(item(choice(either)))
					},
					arrows)

	fun beginOther(name: String): Processor<Token> =
		patternCompiler(
			converter { plus(item(choice(name lineTo it))) },
			false,
			arrows,
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)
}

fun patternCompiler(
	converter: Converter<Pattern, Token> = errorConverter(),
	parent: Boolean = false,
	arrows: PatternArrows = patternArrows(),
	pattern: Pattern = pattern()) =
	PatternCompiler(converter, parent, arrows, pattern)

fun PatternCompiler.plus(item: PatternItem) =
	pattern
		.plus(item)
		.let { arrows.resolve(it) }
		.let {
			if (partial) converter.convert(it)
			else PatternCompiler(converter, partial, arrows, it)
		}

fun PatternCompiler.set(pattern: Pattern) =
	copy(pattern = pattern)
