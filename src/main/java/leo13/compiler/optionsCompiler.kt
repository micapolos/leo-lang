package leo13.compiler

import leo13.*
import leo13.pattern.Options
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.pattern.patternLine
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class OptionsCompiler(
	val converter: Converter<Options, Token>,
	val context: PatternContext,
	val options: Options) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			converter.scriptingLine,
			context.scriptingLine,
			options.scriptingLine)

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				PatternCompiler(
					converter { pattern ->
						OptionsCompiler(
							converter,
							context.trace(token.opening.name.patternLine),
							options.plus(context.definitions.resolve(token.opening.name lineTo pattern)))
					},
					false,
					context,
					pattern())
			is ClosingToken -> converter.convert(options)
		}
}