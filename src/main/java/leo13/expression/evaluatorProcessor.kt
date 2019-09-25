package leo13.expression

import leo13.Converter
import leo13.ObjectScripting
import leo13.Processor
import leo13.interpreter.ValueTyped
import leo13.script.lineTo
import leo13.script.script
import leo13.value.Value
import leo13.value.scriptLine

data class EvaluatorProcessor(
	val parent: Converter<ValueTyped, CompilerToken>,
	val given: ValueGiven,
	val value: Value) : ObjectScripting(), Processor<CompilerToken> {
	override val scriptingLine
		get() =
			"evaluator" lineTo script(
				parent.scriptingLine,
				given.scriptLine,
				value.scriptLine)

	override fun process(token: CompilerToken): Processor<CompilerToken> =
		TODO()
//		when (token) {
//			is BeginCompilerToken ->
//				EvaluatorProcessor(
//					converter { childValue ->
//						EvaluatorProcessor(parent, given, value.plus(token.name lineTo childValue))
//					},
//					given,
//					value())
//			is CompiledCompilerToken ->
//				parent.convert(given.evaluator(evaluated(value)).plus(token.expression).evaluated.value)
//		}
}

fun Converter<ValueTyped, CompilerToken>.evaluator(given: ValueGiven, value: Value) =
	EvaluatorProcessor(this, given, value)