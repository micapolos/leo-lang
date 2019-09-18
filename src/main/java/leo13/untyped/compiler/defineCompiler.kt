package leo13.untyped.compiler

import leo13.*
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.given
import leo13.untyped.pattern.*
import leo13.untyped.value.function
import leo13.untyped.value.value

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
						if (pattern.isEmpty) tracedError("expected" lineTo script("pattern"))
						else patternCompiler(
							converter { rhsPattern ->
								pattern
									.leafPlusOrNull(rhsPattern)
									?.let {
										DefineCompiler(
											converter,
											context.plus(pattern arrowTo it),
											pattern())
									}
									?: tracedError("error" lineTo script("has"))
							},
							false,
							context.arrows)
					"gives" ->
						if (pattern.isEmpty) tracedError("expected" lineTo script("pattern"))
						else compiler(
							converter { bodyCompiled ->
								DefineCompiler(
									converter,
									context.plus(
										compiled(
											function(
												given(value()),
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
						context.arrows).process(token)
				}
			is ClosingToken ->
				if (pattern.isEmpty) converter.convert(context)
				else tracedError("expected" lineTo script("has").plus("or" lineTo script("gives")))
		}
}
