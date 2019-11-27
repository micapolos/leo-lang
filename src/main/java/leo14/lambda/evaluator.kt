package leo14.lambda

data class Evaluator<T>(val resolveFn: T.(Value<T>) -> Value<T>?)

fun <T> evaluator(fn: T.(Value<T>) -> Value<T>?) = Evaluator(fn)
fun <T> nullEvaluator(): Evaluator<T> = evaluator { null }
fun <T> Evaluator<T>.resolve(lhs: T, rhs: Value<T>): Value<T>? = lhs.resolveFn(rhs)

fun <T> Evaluator<T>.evaluate(value: Value<T>): Value<T> =
	when (value.term) {
		is NativeTerm -> null
		is AbstractionTerm -> null
		is ApplicationTerm -> resolve(
			evaluate(value.scope.value(value.term.application.lhs)),
			evaluate(value.scope.value(value.term.application.rhs)))
		is VariableTerm -> value.scope[value.term.variable.index]
	} ?: value

fun <T> Evaluator<T>.resolve(lhs: Value<T>, rhs: Value<T>): Value<T>? =
	when (lhs.term) {
		is NativeTerm -> resolve(lhs.term.native, rhs)
		is AbstractionTerm -> evaluate(lhs.scope.push(rhs).value(lhs.term.abstraction.body))
		else -> null
	}
