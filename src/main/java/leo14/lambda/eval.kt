package leo14.lambda

import leo13.*

sealed class Value<out T>
data class NativeValue<T>(val native: T) : Value<T>()
data class ThunkValue<T>(val thunk: Thunk<T>) : Value<T>()

data class Thunk<out T>(val scope: Scope<T>, val term: Term<T>)
data class Scope<out T>(val valueStack: Stack<Value<T>>)

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(thunk: Thunk<T>): Value<T> = ThunkValue(thunk)

fun <T> thunk(scope: Scope<T>, body: Term<T>) = Thunk(scope, body)

fun <T> emptyScope() = Scope<T>(stack())
fun <T> Scope<T>.push(value: Value<T>) = Scope(valueStack.push(value))
operator fun <T> Scope<T>.get(index: Index) = valueStack.get(index)!!

fun <T> Term<T>.value(scope: Scope<T>): Value<T> =
	when (this) {
		is NativeTerm -> value(native)
		is AbstractionTerm -> value(thunk(scope, abstraction.body))
		is ApplicationTerm -> application.lhs.value(scope).apply(application.rhs.value(scope))
		is VariableTerm -> scope[variable.index]
	}

fun <T> Value<T>.apply(rhs: Value<T>): Value<T> =
	when (this) {
		is NativeValue -> error("$this.apply($rhs)")
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
		if (!scope.valueStack.isEmpty) error("free variables")
		else fn(term)

val <T> Term<T>.value get() = value(emptyScope())

val <T> Term<T>.eval get() = value.evalTerm
