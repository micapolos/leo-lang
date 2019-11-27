package leo14.lambda

data class Evaluator<T>(val resolveFn: T.(Value<T>) -> Value<T>?)

fun <T> evaluator(fn: T.(Value<T>) -> Value<T>?) = Evaluator(fn)
fun <T> nullEvaluator(): Evaluator<T> = evaluator { null }
fun <T> Evaluator<T>.resolve(lhs: T, rhs: Value<T>): Value<T>? = lhs.resolveFn(rhs)

fun <T> Evaluator<T>.evaluate(value: Value<T>): Value<T> =
	value.term.evaluate(value.scope)