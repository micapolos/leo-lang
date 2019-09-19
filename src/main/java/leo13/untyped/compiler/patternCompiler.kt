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
	val definitions: PatternDefinitions,
	val pattern: Pattern
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				"partial" lineTo script("$partial"),
				definitions.scriptingLine,
				pattern.scriptLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String): Processor<Token> =
		when (name) {
			// TODO: "either" should not be allowed after a line without "either"
			"either" -> beginEither
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
									set(link.lhs.plus(choice.plus(either)))
								},
								definitions)
						}
						?: tracedError("not" lineTo script("expected" lineTo script("either")))
				}
				?: eitherCompiler(
					converter { either ->
						plus(item(choice(either)))
					},
					definitions)

	fun beginOther(name: String): Processor<Token> =
		patternCompiler(
			converter { plus(item(choice(name lineTo it))) },
			false,
			definitions,
			pattern())

	val end: Processor<Token> get() = converter.convert(pattern)
}

fun patternCompiler(
	converter: Converter<Pattern, Token> = errorConverter(),
	parent: Boolean = false,
	definitions: PatternDefinitions = patternDefinitions(),
	pattern: Pattern = pattern()) =
	PatternCompiler(converter, parent, definitions, pattern)

fun PatternCompiler.plus(item: PatternItem) =
	item.lineOrNull
		?.let { plus(it) }
		?: set(pattern.plus(item))

fun PatternCompiler.plus(line: PatternLine) =
	set(pattern.plus(definitions.resolve(line)))

fun PatternCompiler.set(newPattern: Pattern) =
	if (partial) converter.convert(newPattern)
	else PatternCompiler(converter, partial, definitions, newPattern)
