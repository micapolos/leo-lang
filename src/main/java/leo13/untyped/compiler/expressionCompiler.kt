package leo13.untyped.compiler

import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token
import leo13.untyped.expression.expression
import leo13.untyped.expression.given
import leo13.untyped.expression.op
import leo13.untyped.pattern.isEmpty

data class ExpressionCompiler(
	val converter: Converter<ExpressionCompiled, Token>,
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
				"define" -> beginOf
				"given" -> beginGiven
				"in" -> beginIn
				"of" -> beginOf
				"previous" -> beginPrevious
				"set" -> beginSet
				"switch" -> beginSwitch
				else -> beginOther(token.opening.name)
			}
			is ClosingToken -> converter.convert(compiled)
		}

	val beginDefine: Processor<Token> get() = TODO()

	val beginGiven: Processor<Token>
		get() =
			compiler(
				converter { plusGiven(it) },
				context)

	val beginIn: Processor<Token>
		get() =
			compiler(
				converter { plusIn(it) },
				context.bind(compiled.pattern))

	val beginOf: Processor<Token> get() = TODO()

	val beginPrevious: Processor<Token>
		get() =
			compiler(
				converter { plusPrevious(it) },
				context)

	val beginSet: Processor<Token> get() = TODO()
	val beginSwitch: Processor<Token> get() = TODO()

	fun beginOther(name: String): Processor<Token> =
		compiler(
			converter { plus(name lineTo it) },
			context)
}

fun expressionCompiler() =
	compiler(errorConverter(), context(), compiled())

fun compiler(
	converter: Converter<ExpressionCompiled, Token>,
	context: Context,
	compiled: ExpressionCompiled = compiled()) =
	ExpressionCompiler(converter, context, compiled)

fun ExpressionCompiler.set(context: Context) =
	copy(context = context)

fun ExpressionCompiler.set(compiled: ExpressionCompiled) =
	copy(compiled = compiled)

// TODO: Implement normalization outside of the compiler, as Processor<Token>
fun ExpressionCompiler.plus(line: CompiledLine): ExpressionCompiler =
	if (line.rhs.pattern.isEmpty) plus(line.name)
	else plusNormalized(line)

fun ExpressionCompiler.plusIn(rhs: ExpressionCompiled): ExpressionCompiler =
	set(compiled.plusIn(rhs))

fun ExpressionCompiler.plusGiven(rhs: ExpressionCompiled): ExpressionCompiler =
	if (!compiled.pattern.isEmpty || !rhs.pattern.isEmpty) tracedError()
	else set(compiled(expression(given.op), context.givenPattern))

fun ExpressionCompiler.plusPrevious(rhs: ExpressionCompiled): ExpressionCompiler =
	if (!compiled.pattern.isEmpty) tracedError()
	else rhs.previousOrNull?.let { set(it) } ?: tracedError()

fun ExpressionCompiler.plus(name: String): ExpressionCompiler =
	set(compiled()).plusNormalized(name lineTo compiled)

fun ExpressionCompiler.plusNormalized(line: CompiledLine): ExpressionCompiler =
	when (line.name) {
		else -> plusOther(line)
	}

fun ExpressionCompiler.plusOther(line: CompiledLine): ExpressionCompiler =
	plusGetOrNull(line) ?: append(line)

fun ExpressionCompiler.plusGetOrNull(line: CompiledLine): ExpressionCompiler? =
	line.rhs.getOrNull(line.name)?.run { set(this) } // TODO: Don't set(), but plus(line)

fun ExpressionCompiler.append(line: CompiledLine): ExpressionCompiler =
	set(compiled.plus(line))
