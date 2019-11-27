package leo14.lambda

import leo13.*

data class Scope<T>(
	val valueStack: Stack<Value<T>>,
	val evaluator: Evaluator<T>)

fun <T> emptyScope(evaluator: Evaluator<T> = nullEvaluator()): Scope<T> =
	Scope(stack(), evaluator)

fun <T> Scope<T>.push(value: Value<T>) = Scope(valueStack.push(value), evaluator)

operator fun <T> Scope<T>.get(index: Index): Value<T>? = valueStack.get(index)
