package leo14.lambda

import leo.base.iterate
import leo13.fold
import leo13.reverse
import leo13.takeOrNull

val <T> Value<T>.evalTerm: Term<T> get() = term
	.iterate(term.freeVariableCount) { fn(this) }
	.fold(scope.valueStack.takeOrNull(term.freeVariableCount)!!.reverse) { invoke(it.evalTerm) }

fun <T> Term<T>.eval(evaluator: Evaluator<T>) = evaluator.evaluate(value(scope(), this)).evalTerm
val <T> Term<T>.eval get() = eval(nullEvaluator())

