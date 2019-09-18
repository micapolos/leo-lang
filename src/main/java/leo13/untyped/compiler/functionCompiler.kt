package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.given
import leo13.untyped.pattern.Pattern
import leo13.untyped.pattern.arrowTo
import leo13.untyped.pattern.isEmpty
import leo13.untyped.pattern.pattern
import leo13.untyped.value.function
import leo13.untyped.value.value

data class FunctionCompiler(
	val converter: Converter<FunctionCompiled, Token>,
	val context: Context,
	val parameterPattern: Pattern,
	val functionCompiledOrNull: FunctionCompiled?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"compiler" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				parameterPattern.scriptingLine,
				functionCompiledOrNull?.scriptingLine ?: "compiled" lineTo script("function" lineTo script("none")))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (functionCompiledOrNull != null) tracedError<Processor<Token>>("expected" lineTo script("end"))
				else if (token.opening.name == "gives")
					if (parameterPattern.isEmpty) tracedError<Processor<Token>>("empty" lineTo script("pattern"))
					else compiler(
						converter { bodyCompiled ->
							FunctionCompiler(
								converter,
								context,
								pattern(),
								compiled(
									function(
										given(value()),
										bodyCompiled.expression),
									parameterPattern arrowTo bodyCompiled.pattern))
						},
						context.bind(parameterPattern))
				else patternCompiler(
					converter { newPattern ->
						FunctionCompiler(
							converter,
							context,
							newPattern,
							functionCompiledOrNull)
					},
					true,
					context.arrows,
					parameterPattern).process(token)
			is ClosingToken -> {
				if (functionCompiledOrNull == null) tracedError("expected" lineTo script("gives"))
				else converter.convert(functionCompiledOrNull)
			}
		}
}
