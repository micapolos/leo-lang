package leo14.lambda.evaluator

import leo13.Stack
import leo13.get
import leo13.push
import leo13.stack
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm

data class Scope<out T>(val valueStack: Stack<Value<T>>)

fun <T> emptyScope(): Scope<T> = Scope(stack())
fun <T> Scope<T>.push(value: Value<T>): Scope<T> = Scope(valueStack.push(value))
fun <T> Scope<T>.at(index: Int): Value<T> = valueStack.get(index)!!

fun <T> Scope<T>.value(term: Term<T>, nativeApply: NativeApply<T>): Value<T> =
	when (term) {
		is NativeTerm -> value(term.native)
		is AbstractionTerm -> value(function(term.abstraction.body))
		is ApplicationTerm -> {
			val lhs = value(term.application.lhs, nativeApply)
			val rhs = value(term.application.rhs, nativeApply)
			lhs.apply(rhs, nativeApply)
		}
		is VariableTerm -> at(term.variable.index)
	}
