package leo14.lambda

import leo13.*

typealias NativeApply<T> = T.(Term<T>) -> Term<T>

fun <T> errorNativeApply(): NativeApply<T> = { error("nativeApply") }

data class Value<T>(val scope: Scope<T>, val term: Term<T>)
data class Scope<T>(val valueStack: Stack<Value<T>>, val nativeApply: NativeApply<T>)

fun <T> value(scope: Scope<T>, body: Term<T>) = Value(scope, body)

fun <T> emptyScope(nativeApply: NativeApply<T> = errorNativeApply()) = Scope(stack(), nativeApply)
fun <T> Scope<T>.push(value: Value<T>) = Scope(valueStack.push(value), nativeApply)
operator fun <T> Scope<T>.get(index: Index) = valueStack.get(index)!!

fun <T> Term<T>.value(scope: Scope<T>): Value<T> =
	when (this) {
		is NativeTerm -> value(scope, this)
		is AbstractionTerm -> value(scope, this)
		is ApplicationTerm -> application.lhs.value(scope).apply(application.rhs.value(scope), scope.nativeApply)
		is VariableTerm -> scope[variable.index]
	}

fun <T> Value<T>.apply(rhs: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (term) {
		is NativeTerm -> value(scope, term.native.nativeApply(rhs.term))
		is AbstractionTerm -> term.abstraction.body.value(scope.push(rhs))
		else -> null
	} ?: error("$this.apply($rhs)")

fun <T> Term<T>.value(nativeApply: NativeApply<T>) = value(emptyScope(nativeApply))
val <T> Term<T>.value get() = value(errorNativeApply())

fun <T> Term<T>.eval(nativeApply: NativeApply<T>) = value(nativeApply).term
val <T> Term<T>.eval get() = eval(errorNativeApply())

