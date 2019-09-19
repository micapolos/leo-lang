package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.pattern.*

data class EitherCompiler(
	val converter: Converter<Either, Token>,
	val arrows: PatternArrows,
	val eitherOrNull: Either?
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				arrows.scriptingLine,
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
			arrows,
			pattern())


	val end
		get() =
			if (eitherOrNull == null) tracedError()
			else converter.convert(eitherOrNull)

	fun plus(either: Either) =
		// TODO: Type arrows should be pattern-line-based?
		arrows.resolve(pattern(either.name lineTo either.rhs)).linkOrNull!!.item.lineOrNull!!.let { resolved ->
			copy(eitherOrNull = resolved.name eitherTo resolved.rhs)
		}
}

fun eitherCompiler(converter: Converter<Either, Token>, arrows: PatternArrows, eitherOrNull: Either? = null) =
	EitherCompiler(converter, arrows, eitherOrNull)

