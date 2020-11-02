package leo14.lambda.evaluator

import leo.base.iterate
import leo13.fold
import leo13.reverse
import leo13.size
import leo13.takeOrNull
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.freeVariableCount
import leo14.lambda.invoke
import kotlin.math.min

data class Function<out T>(val scope: Scope<T>, val bodyTerm: Term<T>)

fun <T> Scope<T>.function(term: Term<T>): Function<T> = Function(this, term)

fun <T> Function<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	scope.push(value).value(bodyTerm, nativeApply)

val <T> Function<T>.term: Term<T>
	get() =
		bodyTerm
			.iterate(bodyTerm.freeVariableCount) { fn(this) }
			.fold(scope.valueStack.takeOrNull(min(bodyTerm.freeVariableCount, (scope.valueStack.size)))!!.reverse) {
				invoke(it.term)
			}
