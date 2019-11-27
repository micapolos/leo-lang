package leo14.lambda

import leo.base.iterate
import leo13.fold
import leo13.reverse
import leo13.takeOrNull

fun <T> Term<T>.value(scope: Scope<T>): Value<T> =
	when (this) {
		is NativeTerm -> value(scope, this)
		is AbstractionTerm -> value(scope, this)
		is ApplicationTerm -> application.lhs.value(scope)
			.apply(application.rhs.value(scope), scope.evaluator)
		is VariableTerm -> scope[variable.index]
	} ?: value(scope, this)

fun <T> Value<T>.apply(rhs: Value<T>, evaluator: Evaluator<T>): Value<T>? =
	when (term) {
		is NativeTerm -> evaluator.resolve(term.native, rhs)
		is AbstractionTerm -> term.abstraction.body.value(scope.push(rhs))
		else -> null
	}

fun <T> Term<T>.value(evaluator: Evaluator<T>): Value<T> = value(emptyScope(evaluator))
val <T> Term<T>.value: Value<T> get() = value(nullEvaluator())

val <T> Value<T>.evalTerm: Term<T> get() = term
	.iterate(term.freeVariableCount) { fn(this) }
	.fold(scope.valueStack.takeOrNull(term.freeVariableCount)!!.reverse) { invoke(it.evalTerm) }

fun <T> Term<T>.eval(evaluator: Evaluator<T>) = value(evaluator).evalTerm
val <T> Term<T>.eval get() = eval(nullEvaluator())

