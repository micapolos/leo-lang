package leo13.compiler

import leo13.*
import leo13.pattern.PatternLine
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class PatternLineCompiler(
	val converter: Converter<PatternLine, Token>,
	val context: PatternContext,
	val patternLineOrNull: PatternLine?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			converter.scriptingLine,
			context.scriptingLine,
			patternLineOrNull?.scriptingLine ?: lineName lineTo script(patternName lineTo script(noneName)))


	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (patternLineOrNull == null) PatternCompiler(
					converter { pattern ->
						PatternLineCompiler(
							converter,
							context,
							context.definitions.resolve(token.opening.name lineTo pattern))
					},
					false,
					context.trace(token.opening.name),
					pattern())
				else tracedError(expectedName lineTo script(endName))
			is ClosingToken ->
				if (patternLineOrNull != null) converter.convert(patternLineOrNull)
				else tracedError(expectedName lineTo script(patternName, lineName))
		}
}