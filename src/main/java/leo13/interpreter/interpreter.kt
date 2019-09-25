package leo13.interpreter

import leo13.*
import leo13.compiler.Compiled
import leo13.compiler.Compiler
import leo13.compiler.compiled
import leo13.compiler.typed
import leo13.expression.expression
import leo13.expression.op
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class Interpreter(
	val converter: Converter<ValueTyped, Token>,
	val processor: Processor<Interpreted>,
	val interpreted: Interpreted) : ObjectScripting(), Processor<Token> {

	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			interpreterName lineTo script(
				processor.scriptingLine,
				interpreted.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken ->
				Compiler(
					errorConverter(),
					processor { process(it) },
					compiled(
						interpreted.context.compilerContext,
						typed(
							expression(op(interpreted.typed.value)),
							interpreted.typed.type)))
					.process(token)
			is ClosingToken -> converter.convert(interpreted.typed)
		}

	fun process(compiled: Compiled) =
		typed(
			interpreted.context.valueContext.evaluate(compiled.typed.expression),
			compiled.typed.type)
			.let { valueTyped ->
				Interpreter(
					converter,
					processor.process(
						interpreted(
							interpreterContext(
								compiled.context,
								interpreted.context.valueContext),
							valueTyped)),
					interpreted(
						interpreted.context,
						valueTyped))
			}
}

fun Processor<Interpreted>.interpreter(interpreted: Interpreted = interpreted()) =
	Interpreter(errorConverter(), this, interpreted)
