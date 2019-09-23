package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.pattern.Pattern
import leo13.pattern.arrowTo
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.value.function

data class FunctionCompiler(
	val converter: Converter<FunctionCompiled, Token>,
	val context: Context,
	val parameterPattern: Pattern,
	val functionCompiledOrNull: FunctionCompiled?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				parameterPattern.scriptingLine,
				functionCompiledOrNull?.scriptingLine ?: compiledName lineTo script(functionName lineTo script(emptyName)))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (functionCompiledOrNull != null) tracedError(expectedName lineTo script(endName))
				else if (token.opening.name == "gives")
					if (parameterPattern.isEmpty) tracedError<Processor<Token>>(emptyName lineTo script(patternName))
					else compiler(
						converter { bodyCompiled ->
							FunctionCompiler(
								converter,
								context,
								pattern(),
								compiled(
									function(
										valueContext(), // TODO
										bodyCompiled.expression),
									parameterPattern arrowTo bodyCompiled.pattern))
						},
						context.give(parameterPattern))
				else patternCompiler(
					converter { newPattern ->
						FunctionCompiler(
							converter,
							context,
							newPattern,
							functionCompiledOrNull)
					},
					true,
					context.patternDefinitions,
					null,
					parameterPattern).process(token)
			is ClosingToken -> {
				if (functionCompiledOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(functionCompiledOrNull)
			}
		}
}
