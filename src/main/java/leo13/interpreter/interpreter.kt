package leo13.interpreter

import leo13.ObjectScripting
import leo13.Processor
import leo13.compiler.ExpressionTyped
import leo13.interpreterName
import leo13.script.lineTo
import leo13.script.script

data class Interpreter(
	val processor: Processor<ValueTyped>,
	val interpreted: Interpreted) : ObjectScripting(), Processor<ExpressionTyped> {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			interpreterName lineTo script(
				processor.scriptingLine,
				interpreted.scriptingLine)

	override fun process(expressionTyped: ExpressionTyped) =
		typed(
			interpreted.context.valueContext.evaluate(expressionTyped.expression),
			expressionTyped.type)
			.let { valueTyped ->
				Interpreter(
					processor.process(valueTyped),
					interpreted(interpreted.context, valueTyped))
			}
}

fun Processor<ValueTyped>.interpreter(interpreted: Interpreted = interpreted()) =
	Interpreter(this, interpreted)
