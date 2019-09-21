package leo13.compiler

import leo13.*
import leo13.pattern.PatternLine
import leo13.pattern.lineTo
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class PatternLineCompiler(
	val converter: Converter<PatternLine, Token>,
	val definitions: PatternDefinitions,
	val patternLineOrNull: PatternLine?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "converter" lineTo script(
			converter.scriptingLine,
			definitions.scriptingLine,
			patternLineOrNull?.scriptingLine ?: "line" lineTo script("pattern" lineTo script("null")))


	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (patternLineOrNull == null) patternCompiler(
					converter { pattern ->
						PatternLineCompiler(
							converter,
							definitions,
							definitions.resolve(token.opening.name lineTo pattern))
					},
					false,
					definitions)
				else tracedError("expected" lineTo script("end"))
			is ClosingToken ->
				if (patternLineOrNull != null) converter.convert(patternLineOrNull)
				else tracedError("expected" lineTo script("pattern", "line"))
		}
}