package leo14.lambda.value

import leo13.Stack
import leo13.get
import leo13.push
import leo13.stack
import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm

val applyTailCallOptimization = true

data class Scope<out T>(val valueStack: Stack<Value<T>>)

fun <T> emptyScope(): Scope<T> = Scope(stack())
fun <T> Scope<T>.push(value: Value<T>): Scope<T> = Scope(valueStack.push(value))
fun <T> Scope<T>.at(index: Int): Value<T> = valueStack.get(index)!!

fun <T> Scope<T>.value(term: Term<T>, nativeApply: NativeApply<T>): Value<T> =
	when (term) {
		is NativeTerm -> value(term.native)
		is AbstractionTerm -> value(function(term.abstraction.body))
		is ApplicationTerm -> value(term.application.lhs, value(term.application.rhs, nativeApply), nativeApply)
		is VariableTerm -> at(term.variable.index)
	}

tailrec fun <T> Scope<T>.value(lhs: Term<T>, rhsValue: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	if (applyTailCallOptimization && lhs is AbstractionTerm && lhs.abstraction.body is ApplicationTerm) {
		val rhsScope = push(rhsValue)
		rhsScope.value(
			lhs.abstraction.body.application.lhs,
			rhsScope.value(lhs.abstraction.body.application.rhs, nativeApply),
			nativeApply)
	} else {
		value(lhs, nativeApply).apply(rhsValue, nativeApply)
	}
