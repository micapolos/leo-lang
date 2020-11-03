package leo14.lambda.value

import leo14.lambda.arg0
import leo14.lambda.arg1
import leo14.lambda.arg2
import leo14.lambda.invoke

fun <T, R> Value<T>.function(fn: (Function<T>) -> R): R =
	when (this) {
		is FunctionValue -> fn(function)
		else -> error("$this as function")
	}

fun <T, R> Value<T>.native(fn: (T) -> R): R =
	when (this) {
		is NativeValue -> fn(native)
		else -> error("$this as native")
	}

val <T> Value<T>.pair: Pair<Value<T>, Value<T>>
	get() =
		function { function ->
			if (function.bodyTerm == arg0<T>()(arg2())(arg1())) function.scope.at(1) to function.scope.at(0)
			else error("$this not pair")
		}
