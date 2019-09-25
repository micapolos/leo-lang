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
	val converter: Converter<TypedFunction, Token>,
	val context: Context,
	val parameterType: Type,
	val typedFunctionOrNull: TypedFunction?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				parameterType.scriptingLine,
				typedFunctionOrNull?.scriptingLine ?: typedName lineTo script(functionName lineTo script(emptyName)))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (typedFunctionOrNull != null) tracedError(expectedName lineTo script(endName))
				else if (token.opening.name == "gives")
					if (parameterType.isEmpty) tracedError<Processor<Token>>(emptyName lineTo script(typeName))
					else compiler(
						converter { typedBody ->
							FunctionCompiler(
								converter,
								context,
								type(),
								typed(
									function(
										valueContext(), // TODO
										typedBody.expression),
									parameterType arrowTo typedBody.type))
						},
						context.give(parameterType))
				else TypeCompiler(
					converter { newType ->
						FunctionCompiler(
							converter,
							context,
							newType,
							typedFunctionOrNull)
					},
					true,
					typeContext(context),
					parameterType).process(token)
			is ClosingToken -> {
				if (typedFunctionOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(typedFunctionOrNull)
			}
		}
}
