package leo13.compiler

import leo13.*
import leo13.expression.valueContext
import leo13.pattern.Pattern
import leo13.pattern.arrowTo
import leo13.pattern.pattern
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.value.function

data class DefineCompiler(
	val converter: Converter<Context, Token>,
	val context: Context,
	val pattern: Pattern) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = compilerName lineTo script(
			defineName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				pattern.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken ->
				when (token.opening.name) {
					hasName ->
						pattern
							.linkOrNull
							?.let { patternLink ->
								if (!patternLink.lhs.isEmpty) tracedError(expectedName lineTo script(lineName lineTo script(patternName)))
								else patternLink
									.line
									.let { patternLine ->
										patternCompiler(
											converter { rhsPattern ->
												patternLine
													.leafPlusOrNull(rhsPattern)
													?.let { fullPatternLine ->
														DefineCompiler(
															converter,
															context.plus(definition(patternLine, rhsPattern)).plus(fullPatternLine),
															pattern())
													}
													?: tracedError(errorName lineTo script(hasName))
											},
											false,
											context.patternDefinitions)
									}
							}
							?: tracedError(expectedName lineTo script(patternName))
					givesName ->
						if (pattern.isEmpty) tracedError(expectedName lineTo script(patternName))
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
							context.give(pattern))
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
				else tracedError(expectedName lineTo script(optionsName lineTo script(hasName, givesName)))
		}
}
