package leo13.untyped.compiler

import leo13.Converter
import leo13.ObjectScripting
import leo13.Processor
import leo13.converter
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class ExpressionCompiler(
	val converter: Converter<ExpressionCompiled, Processor<Token>>,
	val context: Context,
	val compiled: ExpressionCompiled
) :
	ObjectScripting(),
	Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"expression" lineTo script(
				"compiler" lineTo script(
					converter.scriptingLine,
					context.scriptingLine,
					compiled.scriptingLine))

	override fun process(token: Token): Processor<Token> =
		when (token) {
			is OpeningToken -> when (token.opening.name) {
				"define" -> beginDefine
				"of" -> beginOf
				"set" -> beginSet
				"switch" -> beginSwitch
				else -> beginOther(token.opening.name)
			}
			is ClosingToken -> converter.convert(compiled)
		}

	val beginDefine: Processor<Token> get() = TODO()
	val beginOf: Processor<Token> get() = TODO()
	val beginSet: Processor<Token> get() = TODO()
	val beginSwitch: Processor<Token> get() = TODO()

	fun beginOther(name: String): Processor<Token> =
		converter<ExpressionCompiler, ExpressionCompiled, Processor<Token>> {
			converter.expressionCompiler(context, compiled.plus(name lineTo it))
		}.expressionCompiler(context)
}

fun Converter<ExpressionCompiled, Processor<Token>>.expressionCompiler(
	context: Context = context(),
	compiled: ExpressionCompiled = compiled()
) = ExpressionCompiler(this, context, compiled)

