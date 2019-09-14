package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.pattern.isEmpty

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
		compiler(
			converter { plus(name lineTo it) },
			context,
			compiled())
}

fun expressionCompiler() =
	compiler(errorConverter(), context(), compiled())

fun compiler(
	converter: Converter<ExpressionCompiled, Processor<Token>>,
	context: Context,
	compiled: ExpressionCompiled) =
	ExpressionCompiler(converter, context, compiled)

fun ExpressionCompiler.set(compiled: ExpressionCompiled) =
	copy(compiled = compiled)

fun ExpressionCompiler.plus(line: CompiledLine): ExpressionCompiler =
	if (line.rhs.pattern.isEmpty) plus(line.name)
	else plusNormalized(line)

fun ExpressionCompiler.plus(name: String): ExpressionCompiler =
	set(compiled()).plusNormalized(name lineTo compiled)

fun ExpressionCompiler.plusNormalized(line: CompiledLine): ExpressionCompiler =
	set(compiled.plus(line))