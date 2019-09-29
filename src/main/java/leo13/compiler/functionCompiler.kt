package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.script.lineTo
import leo13.script.script
import leo13.token.*
import leo13.type.*
import leo13.value.function

data class FunctionCompiler(
	val converter: Converter<FunctionTyped, Token>,
	val context: Context,
	val parameterType: Type,
	val ofOrNull: TypeOf?,
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
					ofName -> beginOf()
					givesName -> beginGives()
					else -> beginOther(token.opening.name)
				}
			is ClosingToken -> {
				if (typedFunctionOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(typedFunctionOrNull)
			}
		}

	fun beginOf(): Processor<Token> =
		if (ofOrNull != null)
			tracedError(notName lineTo script(expectedName lineTo script(ofName)))
		else TypeCompiler(
			converter { type ->
				FunctionCompiler(
					converter,
					context,
					parameterType,
					of(type),
					typedFunctionOrNull)
			},
			false,
			typeContext(context),
			type())

	fun beginGives() =
		if (parameterType.isEmpty) tracedError<Processor<Token>>(emptyName lineTo script(typeName))
		else Compiler(
			converter { typedBody ->
				if (ofOrNull != null && ofOrNull.type != typedBody.type)
					tracedError(mismatchName lineTo script(
						expectedName lineTo script(ofOrNull.type.scriptingLine),
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
					ofOrNull,
					typedFunctionOrNull)
			},
			true,
			typeContext(context),
			parameterType).process(token(opening(name)))
}
