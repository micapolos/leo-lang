package leo13.interpreter

import leo13.*
import leo13.compiler.compiler
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
						context
							.valueContext
							.evaluate(compiled.expression)
							.let { value ->
								Interpreter(
									converter,
									context,
									interpreted(value, compiled.pattern))
							}
					},
					context.compilerContext)
			is ClosingToken ->
				converter.convert(interpreted)
		}
}

fun interpreter() = Interpreter(errorConverter(), interpreterContext(), interpreted())