package leo13.compiler

import leo13.*
import leo13.pattern.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class OptionsCompiler(
	val converter: Converter<Options, Token>,
	val definitions: PatternDefinitions,
	val recurseDefinitionOrNull: RecurseDefinition?,
	val traceOrNull: PatternTrace?,
	val options: Options) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			converter.scriptingLine,
			definitions.scriptingLine,
			options.scriptingLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				PatternCompiler(
					converter { pattern ->
						OptionsCompiler(
							converter,
							definitions,
							recurseDefinitionOrNull?.recurseIncrease,
							traceOrNull,
							options.plus(definitions.resolve(token.opening.name lineTo pattern)))
					},
					false,
					definitions,
					recurseDefinitionOrNull,
					traceOrNull,
					pattern())
			is ClosingToken -> converter.convert(options)
		}
}