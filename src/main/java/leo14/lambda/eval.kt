package leo14.lambda

import leo.base.iterate
import leo13.fold
import leo13.reverse
import leo13.takeOrNull

fun <T> Term<T>.value(scope: Scope<T>): Value<T> =
	when (this) {
		is NativeTerm -> value(scope, this)
		is AbstractionTerm -> value(scope, this)
		is ApplicationTerm -> application.lhs.value(scope)
			.apply(application.rhs.value(scope), scope.nativeApply)
		is VariableTerm -> scope[variable.index]
	}

fun <T> Value<T>.apply(rhs: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (term) {
		is NativeTerm -> term.native.nativeApply(rhs)
		is AbstractionTerm -> term.abstraction.body.value(scope.push(rhs))
		else -> null
	} ?: error("$this.apply($rhs)")

fun <T> Term<T>.value(nativeApply: NativeApply<T>): Value<T> = value(emptyScope(nativeApply))
val <T> Term<T>.value: Value<T> get() = value(errorNativeApply())

val <T> Value<T>.evalTerm: Term<T> get() = term
	.iterate(term.freeVariableCount) { fn(this) }
	.fold(scope.valueStack.takeOrNull(term.freeVariableCount)!!.reverse) { invoke(it.evalTerm) }

fun <T> Term<T>.eval(nativeApply: NativeApply<T>) = value(nativeApply).evalTerm
val <T> Term<T>.eval get() = eval(errorNativeApply())

