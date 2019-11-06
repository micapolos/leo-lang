package leo14.lambda.eval

import leo13.get
import leo13.stack
import leo14.Script
import leo14.compile
import leo14.lambda.*

val Term.eval get() = eval(stack())

fun Term.eval(stack: Stack): Any =
	when (this) {
		is NativeTerm -> native
		is AbstractionTerm -> abstraction.eval(stack)
		is ApplicationTerm -> application.eval(stack)
		is VariableTerm -> variable.eval(stack)
	}

fun Abstraction<Term>.eval(stack: Stack) = function(stack, body)
fun Application<Term>.eval(stack: Stack) = (lhs.eval(stack) as Function).invoke(rhs.eval(stack))
fun Variable<Any>.eval(stack: Stack) = stack.get(index)!!

val Script.eval
	get() =
		compiler.compile<Term>(this).eval