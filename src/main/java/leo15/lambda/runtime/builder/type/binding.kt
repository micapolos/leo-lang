package leo15.lambda.runtime.builder.type

import leo.base.ifOrNull
import leo.base.runIf
import leo15.lambda.runtime.builder.Term
import leo15.lambda.runtime.builder.get
import leo15.lambda.runtime.builder.invoke

data class Binding<V, T>(val type: Type<T>, val bound: Bound<V, T>)
data class Bound<V, T>(val typed: Typed<V, T>, val isLambda: Boolean)

infix fun <V, T> Type<T>.bindingTo(bound: Bound<V, T>) = Binding(this, bound)
val <V, T> Typed<V, T>.constantBound get() = Bound(this, isLambda = false)
val <V, T> Typed<V, T>.lambdaBound get() = Bound(this, isLambda = true)

fun <V, T> Binding<V, T>.apply(index: Int, typed: Typed<V, T>): Typed<V, T>? =
	ifOrNull(type == typed.type) {
		bound.applyTerm(index, typed.term)?.of(bound.typed.type)
	}

fun <V, T> Bound<V, T>.applyTerm(index: Int, param: Term<V>): Term<V>? =
	get<V>(index).runIf(isLambda) { invoke(param) }
