package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.type.Type
import leo13.type.arrowTo
import leo13.type.type
import leo13.value.function

data class DefineCompiler(
	val converter: Converter<Context, Token>,
	val context: Context,
	val type: Type) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			defineName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				type.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				when (token.opening.name) {
					containsName ->
						type.linkOrNull
							?.let { typeLink ->
								if (!typeLink.lhs.isEmpty) tracedError(expectedName lineTo script(lineName lineTo script(typeName)))
								else typeLink
									.item
									.unexpandedLineOrNull
									?.let { typeLine ->
										TypeCompiler(
											converter { rhsType ->
												typeLine
													.leafPlusOrNull(rhsType)
													?.let { fullTypeLine ->
														DefineCompiler(
															converter,
															context.plus(definition(typeLine, rhsType)).plus(fullTypeLine),
															type())
													}
													?: tracedError(errorName lineTo script(containsName))
											},
											false,
											typeContext(context).copy(trace = type(typeLine).leafNameTraceOrNull()!!),
											type())
									}
							}
							?: tracedError(expectedName lineTo script(typeName))
					givesName ->
						if (type.isEmpty) tracedError(expectedName lineTo script(typeName))
						else Compiler(
							converter { typedBody ->
								DefineCompiler(
									converter,
									context.plus(
										typed(
											function(
												valueContext(), // TODO()
												typedBody.expression),
											type arrowTo typedBody.type,
											recursive(false))),
									type())
							},
							null,
							compiled(context.give(type)))
					else -> TypeCompiler(
						converter { lhsType ->
							DefineCompiler(
								converter,
								context,
								lhsType)
						},
						true,
						typeContext(context),
						type).process(token)
				}
			is ClosingToken ->
				if (type.isEmpty) converter.convert(context)
				else tracedError(expectedName lineTo script(optionsName lineTo script(containsName, givesName)))
		}
}
