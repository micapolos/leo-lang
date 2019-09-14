package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.eitherName
import leo13.untyped.pattern.*

data class ChoiceCompiler(
	val converter: Converter<Choice, Token>,
	val choice: Choice) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				choice.scriptLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end
		}

	fun begin(name: String) =
		if (name != eitherName) tracedError()
		else eitherCompiler(converter { plus(it) })

	val end get() = converter.convert(choice)
}

fun choiceCompiler(
	converter: Converter<Choice, Token> = errorConverter(),
	choice: Choice = choice()) =
	ChoiceCompiler(converter, choice)

fun ChoiceCompiler.plus(either: Either) =
	copy(choice = choice.plus(either))
