package leo13.compiler

import leo13.*
import leo13.pattern.Either
import leo13.pattern.eitherTo
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class EitherCompiler(
	val converter: Converter<Either, Token>,
	val definitions: PatternDefinitions,
	val eitherOrNull: Either?
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				definitions.scriptingLine,
				eitherOrNull?.scriptingLine ?: "either" lineTo script("null"))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		if (eitherOrNull != null) tracedError()
		else patternCompiler(
			converter { plus(name eitherTo it) },
			false,
			definitions,
			pattern())


	val end
		get() =
			if (eitherOrNull == null) tracedError()
			else converter.convert(eitherOrNull)

	fun plus(either: Either) =
		// TODO: Type arrows should be pattern-line-based?
		definitions.resolve(either.name lineTo either.rhs).let {
			copy(eitherOrNull = it.name eitherTo it.rhs)
		}
}

fun eitherCompiler(converter: Converter<Either, Token>, definitions: PatternDefinitions, eitherOrNull: Either? = null) =
	EitherCompiler(converter, definitions, eitherOrNull)

