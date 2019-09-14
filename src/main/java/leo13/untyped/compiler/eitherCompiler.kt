package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.pattern.Either
import leo13.untyped.pattern.eitherTo
import leo13.untyped.pattern.pattern

data class EitherCompiler(
	val converter: Converter<Either, Token>,
	val eitherOrNull: Either?
) :
	ObjectScripting(),
	Processor<Token> {
	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				"either" lineTo script("todo"))

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		if (eitherOrNull != null) tracedError()
		else compiler(
			converter { plus(name eitherTo it) },
			pattern())


	val end
		get() =
			if (eitherOrNull == null) tracedError()
			else converter.convert(eitherOrNull)

	fun plus(either: Either) =
		copy(eitherOrNull = either)
}

fun eitherCompiler(converter: Converter<Either, Token>, eitherOrNull: Either? = null) =
	EitherCompiler(converter, eitherOrNull)

