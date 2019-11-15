package leo14.lambda

import leo13.*

typealias NativeApply<T> = T.(T) -> T

fun <T> errorNativeApply(): NativeApply<T> = { error("nativeApply") }

sealed class Value<out T>
data class NativeValue<T>(val native: T) : Value<T>()
data class ThunkValue<T>(val thunk: Thunk<T>) : Value<T>()

data class Thunk<T>(val scope: Scope<T>, val term: Term<T>)
data class Scope<T>(val valueStack: Stack<Value<T>>, val nativeApply: NativeApply<T>)

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(thunk: Thunk<T>): Value<T> = ThunkValue(thunk)

fun <T> thunk(scope: Scope<T>, body: Term<T>) = Thunk(scope, body)

fun <T> emptyScope(nativeApply: NativeApply<T> = errorNativeApply()) = Scope(stack(), nativeApply)
fun <T> Scope<T>.push(value: Value<T>) = Scope(valueStack.push(value), nativeApply)
operator fun <T> Scope<T>.get(index: Index) = valueStack.get(index)!!

fun <T> Term<T>.value(scope: Scope<T>): Value<T> =
	when (this) {
		is NativeTerm -> value(native)
		is AbstractionTerm -> value(thunk(scope, abstraction.body))
		is ApplicationTerm -> application.lhs.value(scope).apply(application.rhs.value(scope), scope.nativeApply)
		is VariableTerm -> scope[variable.index]
	}

fun <T> Value<T>.apply(rhs: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (this) {
		is NativeValue ->
			when (rhs) {
				is NativeValue -> value(this.native.nativeApply(rhs.native))
				is ThunkValue -> error("$this.apply($rhs)")
			}
		is ThunkValue -> thunk.term.value(thunk.scope.push(rhs))
	}

val <T> Value<T>.evalTerm: Term<T>
	get() =
		when (this) {
			is NativeValue -> term(native)
			is ThunkValue -> thunk.evalTerm
		}

val <T> Thunk<T>.evalTerm: Term<T>
	get() =
		fn(term)

fun <T> Term<T>.value(nativeApply: NativeApply<T>) = value(emptyScope(nativeApply))
val <T> Term<T>.value get() = value(errorNativeApply())

fun <T> Term<T>.eval(nativeApply: NativeApply<T>) = value(nativeApply).evalTerm
val <T> Term<T>.eval get() = eval(errorNativeApply())
