package leo14.lambda.syntax

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo.base.notNullIf
import leo13.Stack
import leo13.indexed
import leo13.mapFirst
import leo13.push
import leo13.seq
import leo13.stack
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.nativeTerm

sealed class Syntax<out T>
data class NativeSyntax<T>(val native: T) : Syntax<T>()
class VariableSyntax<T> : Syntax<T>()
data class InvokeSyntax<T>(val lhs: Syntax<T>, val rhs: Syntax<T>) : Syntax<T>()
data class FunctionSyntax<T>(val func: (Syntax<T>) -> Syntax<T>) : Syntax<T>()

val <T> Syntax<T>.term get() = term(stack())

fun <T> Syntax<T>.term(variableStack: Stack<VariableSyntax<T>>): Term<T> =
	when (this) {
		is NativeSyntax -> nativeTerm(native)
		is VariableSyntax -> arg(variableStack.seq.indexOf(this))
		is InvokeSyntax -> lhs.term(variableStack).invoke(rhs.term(variableStack))
		is FunctionSyntax -> VariableSyntax<T>().let { variable ->
			fn(func.invoke(variable).term(variableStack.push(variable)))
		}
	}
