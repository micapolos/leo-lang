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
import leo13.token.*
import leo13.tokenizer.EmptyTokenizer
import leo13.type.type
import leo13.value.value

data class Interpreter(
	val converter: Converter<ValueTyped, Token>,
	val processor: Processor<Interpreted>,
	val interpreted: Interpreted) : ObjectScripting(), Processor<Token> {

	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			interpreterName lineTo script(
				converter.scriptingLine,
				processor.scriptingLine,
				interpreted.scriptingLine)

	override fun process(token: Token) =
		when (token) {
			is OpeningToken -> begin(token.opening.name)
			is ClosingToken -> end()
		}

	fun begin(name: String) =
		when (name) {
			interpreterName -> beginInterpreter()
			else -> beginOther(name)
		}

	fun end() = converter.convert(interpreted.typed)

	fun beginInterpreter() =
		EmptyTokenizer(
			converter {
				process(
					interpreted(
						interpreted.context,
						typed(
							script(scriptingLine).value,
							script(scriptingLine).type)))
			})

	fun beginOther(name: String) =
		Compiler(
			errorConverter(),
			processor { process(it) },
			compiled(
				interpreted.context.compilerContext,
				typed(
					expression(op(interpreted.typed.value)),
					interpreted.typed.type)))
			.process(token(opening(name)))

	fun process(compiled: Compiled) =
		typed(
			interpreted.context.valueContext.evaluate(compiled.typed.expression),
			compiled.typed.type)
			.let { valueTyped ->
				process(
					interpreted(
						interpreterContext(
							compiled.context,
							interpreted.context.valueContext),
						valueTyped))
			}

	fun process(interpreted: Interpreted) =
		Interpreter(
			converter,
			processor.process(interpreted),
			interpreted)
}

fun Processor<Interpreted>.interpreter(interpreted: Interpreted = interpreted()) =
	Interpreter(errorConverter(), this, interpreted)

fun Converter<ValueTyped, Token>.interpreter(interpreted: Interpreted = interpreted()) =
	Interpreter(this, voidProcessor(), interpreted)
