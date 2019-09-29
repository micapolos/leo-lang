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
	val recursive: BooleanRecursive,
	val parameterType: Type,
	val toOrNull: TypeTo?,
	val typedOrNull: FunctionTyped?) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			compilerName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				recursive.scriptingLine,
				parameterType.scriptingLine,
				toOrNull?.scriptingLine ?: toName lineTo script(noneName),
				typedOrNull?.scriptingLine ?: typedName lineTo script(functionName lineTo script(emptyName)))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				if (typedOrNull != null) tracedError(expectedName lineTo script(endName))
				else when (token.opening.name) {
					recursiveName -> beginRecursive()
					toName -> beginTo()
					givesName -> beginGives()
					else -> beginOther(token.opening.name)
				}
			is ClosingToken -> {
				if (typedOrNull == null) tracedError(expectedName lineTo script(givesName))
				else converter.convert(typedOrNull)
			}
		}

	fun beginRecursive() =
		if (recursive.boolean) tracedError(notName lineTo script(expectedName lineTo script(recursiveName)))
		else FunctionCompiler(
			converter { typed ->
				FunctionCompiler(
					converter,
					context,
					recursive,
					parameterType,
					toOrNull,
					typed)
			},
			context,
			recursive(true),
			parameterType,
			toOrNull,
			typedOrNull)

	fun beginTo(): Processor<Token> =
		if (toOrNull != null)
			tracedError(notName lineTo script(expectedName lineTo script(ofName)))
		else TypeCompiler(
			converter { type ->
				FunctionCompiler(
					converter,
					context,
					recursive,
					parameterType,
					leo13.type.to(type),
					typedOrNull)
			},
			false,
			typeContext(context),
			type())

	fun beginGives() =
		Compiler(
			converter { typedBody ->
				if (toOrNull != null && toOrNull.type != typedBody.type)
					tracedError(mismatchName lineTo script(
						expectedName lineTo script(toOrNull.type.scriptingLine),
						actualName lineTo script(typedBody.type.scriptingLine)))
				else FunctionCompiler(
					converter,
					context,
					recursive,
					type(),
					toOrNull,
					typed(
						function(
							valueContext(), // TODO
							typedBody.expression),
						parameterType arrowTo typedBody.type,
						recursive))
			},
			null,
			compiled(context.give(parameterType)))

	fun beginOther(name: String) =
		if (toOrNull != null) tracedError(expectedName lineTo script(givesName))
		else TypeCompiler(
			converter { newType ->
				FunctionCompiler(
					converter,
					context,
					recursive,
					newType,
					toOrNull,
					typedOrNull)
			},
			true,
			typeContext(context),
			parameterType).process(token(opening(name)))
}
