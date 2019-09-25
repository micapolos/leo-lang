package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.type.Type
import leo13.type.arrowTo
import leo13.type.type
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.value.function

data class FunctionCompiler(
	val converter: Converter<FunctionCompiled, Token>,
	val context: Context,
	val parameterType: Type,
	val functionCompiledOrNull: FunctionCompiled?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				parameterType.scriptingLine,
				functionCompiledOrNull?.scriptingLine ?: compiledName lineTo script(functionName lineTo script(emptyName)))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (functionCompiledOrNull != null) tracedError(expectedName lineTo script(endName))
				else if (token.opening.name == "gives")
					if (parameterType.isEmpty) tracedError<Processor<Token>>(emptyName lineTo script(typeName))
					else compiler(
						converter { bodyCompiled ->
							FunctionCompiler(
								converter,
								context,
								type(),
								compiled(
									function(
										valueContext(), // TODO
										bodyCompiled.expression),
									parameterType arrowTo bodyCompiled.type))
						},
						context.give(parameterType))
				else TypeCompiler(
					converter { newType ->
						FunctionCompiler(
							converter,
							context,
							newType,
							functionCompiledOrNull)
					},
					true,
					typeContext(context),
					parameterType).process(token)
			is ClosingToken -> {
				if (functionCompiledOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(functionCompiledOrNull)
			}
		}
}
