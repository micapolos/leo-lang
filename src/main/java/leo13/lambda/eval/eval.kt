package leo13.lambda.eval

import leo13.get
import leo13.lambda.*
import leo13.stack

val Value.eval get() = eval(stack())

fun Value.eval(stack: Stack): Any =
	when (this) {
		is NativeValue -> native
		is AbstractionValue -> abstraction.eval(stack)
		is ApplicationValue -> application.eval(stack)
		is VariableValue -> variable.eval(stack)
	}

fun Abstraction<Value>.eval(stack: Stack) = function(stack, body)
fun Application<Value>.eval(stack: Stack) = (lhs.eval(stack) as Function)(rhs.eval(stack))
fun Variable<Any>.eval(stack: Stack) = stack.get(index)!!
