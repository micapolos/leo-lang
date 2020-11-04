package leo14.lambda.value

import leo14.lambda.abstraction
import leo14.lambda.application
import leo14.lambda.variable

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
		function.let { function ->
			function.bodyTerm.application { lhs, arg0 ->
				arg0.variable(1) {
					lhs.application { lhs, arg1 ->
						arg1.variable(2) {
							lhs.variable(0) {
								function.scope.at(1) to function.scope.at(0)
							}
						}
					}
				}
			}
		}

fun <T, R> Value<T>.switch(firstFn: (Value<T>) -> R, secondFn: (Value<T>) -> R): R =
	function.let { function ->
		function.scope.at(0).let { value ->
			function.bodyTerm.abstraction { body ->
				body.application { lhs, rhs ->
					lhs.variable { index ->
						when (index) {
							0 -> rhs.abstraction { body ->
								firstFn(value(function.copy(bodyTerm = body)))
							}
							1 -> rhs.variable(0) {
								secondFn(value)
							}
							else -> null!!
						}
					}
				}
			}
		}
	}