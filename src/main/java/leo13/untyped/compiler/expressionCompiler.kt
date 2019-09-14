package leo13.untyped.compiler

import leo13.*
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
			converter.compilerTo(context, compiled.plus(name lineTo it))
		}.compilerTo(context)
}

fun expressionCompiler(
	converter: Converter<ExpressionCompiled, Processor<Token>> = errorConverter(),
	context: Context = context(),
	compiled: ExpressionCompiled = compiled()) =
	ExpressionCompiler(converter, context, compiled)

fun Converter<ExpressionCompiled, Processor<Token>>.compilerTo(
	context: Context = context(),
	compiled: ExpressionCompiled = compiled()
) = ExpressionCompiler(this, context, compiled)

fun ExpressionCompiler.set(compiled: ExpressionCompiled) =
	copy(compiled = compiled)