package leo13.compiler

import leo13.Converter
import leo13.ObjectScripting
import leo13.Processor
import leo13.converter
import leo13.pattern.Choice
import leo13.pattern.lineTo
import leo13.pattern.plus
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class ChoiceCompiler(
	val converter: Converter<Choice, Token>,
	val definitions: PatternDefinitions,
	val choice: Choice) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "compiler" lineTo script(
			converter.scriptingLine,
			definitions.scriptingLine,
			choice.scriptingLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				patternCompiler(
					converter { pattern ->
						ChoiceCompiler(
							converter,
							definitions,
							choice.plus(definitions.resolve(token.opening.name lineTo pattern)))
					},
					false,
					definitions)
			is ClosingToken -> converter.convert(choice)
		}
}