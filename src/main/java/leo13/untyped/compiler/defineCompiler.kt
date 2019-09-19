package leo13.untyped.compiler

import leo13.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.valueContext
import leo13.untyped.pattern.*
import leo13.untyped.value.function

data class DefineCompiler(
	val converter: Converter<Context, Token>,
	val context: Context,
	val pattern: Pattern) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "compiler" lineTo script(
			"define" lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				pattern.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				when (token.opening.name) {
					"has" ->
						pattern
							.linkOrNull
							?.let { patternLink ->
								if (!patternLink.lhs.isEmpty) tracedError("expected" lineTo script("line" lineTo script("pattern")))
								else patternLink
									.item
									.lineOrNull
									?.let { patternLine ->
										patternCompiler(
											converter { rhsPattern ->
												pattern
													.leafPlusOrNull(rhsPattern)
													?.let { fullPattern ->
														DefineCompiler(
															converter,
															context.plus(definition(patternLine, rhsPattern)).plus(fullPattern),
															pattern())
													}
													?: tracedError("error" lineTo script("has"))
											},
											false,
											context.patternDefinitions)
									}
							}
							?: tracedError("expected" lineTo script("pattern"))
					"gives" ->
						if (pattern.isEmpty) tracedError("expected" lineTo script("pattern"))
						else compiler(
							converter { bodyCompiled ->
								DefineCompiler(
									converter,
									context.plus(
										compiled(
											function(
												valueContext(), // TODO()
												bodyCompiled.expression),
											pattern arrowTo bodyCompiled.pattern)),
									pattern())
							},
							context.bind(pattern))
					else -> patternCompiler(
						converter { lhsPattern ->
							DefineCompiler(
								converter,
								context,
								lhsPattern)
						},
						true,
						context.patternDefinitions,
						pattern).process(token)
				}
			is ClosingToken ->
				if (pattern.isEmpty) converter.convert(context)
				else tracedError("expected" lineTo script("has").plus("or" lineTo script("gives")))
		}
}
