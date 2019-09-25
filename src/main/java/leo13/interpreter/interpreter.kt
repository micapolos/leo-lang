package leo13.interpreter

import leo13.*
import leo13.compiler.compiled
import leo13.compiler.compiler
import leo13.expression.expression
import leo13.expression.op
import leo13.script.lineTo
import leo13.script.script
import leo13.token.ClosingToken
import leo13.token.OpeningToken
import leo13.token.Token

data class Interpreter(
	val converter: Converter<Interpreted, Token>,
	val context: InterpreterContext,
	val interpreted: Interpreted) : ObjectScripting(), Processor<Token> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			interpreterName lineTo script(
				converter.scriptingLine,
				context.scriptingLine,
				interpreted.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken ->
				compiler(
					converter { compiled ->
						Interpreter(
							converter,
							context,
							interpreted(
								context.valueContext.evaluate(compiled.expression),
								compiled.pattern))
					},
					context.compilerContext,
					compiled(
						expression(op(interpreted.value)),
						interpreted.pattern))
					.process(token)
			is ClosingToken ->
				converter.convert(interpreted)
		}
}

fun Converter<Interpreted, Token>.interpreter() =
	Interpreter(this, interpreterContext(), interpreted())
