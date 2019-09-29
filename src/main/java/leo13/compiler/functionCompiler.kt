package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.script.lineTo
import leo13.script.script
import leo13.token.*
import leo13.type.Type
import leo13.type.TypeTo
import leo13.type.arrowTo
import leo13.type.type
import leo13.value.function

data class FunctionCompiler(
	val converter: Converter<FunctionTyped, Token>,
	val context: Context,
	val parameterType: Type,
	val toOrNull: TypeTo?,
	val typedFunctionOrNull: FunctionTyped?) : ObjectScripting(), Processor<Token> {
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
				else when (token.opening.name) {
					toName -> beginTo()
					givesName -> beginGives()
					else -> beginOther(token.opening.name)
				}
			is ClosingToken -> {
				if (typedFunctionOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(typedFunctionOrNull)
			}
		}

	fun beginTo(): Processor<Token> =
		if (toOrNull != null)
			tracedError(notName lineTo script(expectedName lineTo script(ofName)))
		else TypeCompiler(
			converter { type ->
				FunctionCompiler(
					converter,
					context,
					parameterType,
					leo13.type.to(type),
					typedFunctionOrNull)
			},
			false,
			typeContext(context),
			type())

	fun beginGives() =
		if (parameterType.isEmpty) tracedError<Processor<Token>>(emptyName lineTo script(typeName))
		else Compiler(
			converter { typedBody ->
				if (toOrNull != null && toOrNull.type != typedBody.type)
					tracedError(mismatchName lineTo script(
						expectedName lineTo script(toOrNull.type.scriptingLine),
						actualName lineTo script(typedBody.type.scriptingLine)))
				else FunctionCompiler(
					converter,
					context,
					type(),
					null,
					typed(
						function(
							valueContext(), // TODO
							typedBody.expression),
						parameterType arrowTo typedBody.type))
			},
			null,
			compiled(context.give(parameterType)))

	fun beginOther(name: String) =
		TypeCompiler(
			converter { newType ->
				FunctionCompiler(
					converter,
					context,
					newType,
					toOrNull,
					typedFunctionOrNull)
			},
			true,
			typeContext(context),
			parameterType).process(token(opening(name)))
}
