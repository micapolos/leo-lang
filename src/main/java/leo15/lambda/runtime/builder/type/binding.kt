package leo15.lambda.runtime.builder.type

import leo.base.ifOrNull
import leo15.lambda.runtime.builder.Term
import leo15.lambda.runtime.builder.get
import leo15.lambda.runtime.builder.invoke

data class Binding<T>(val arrow: Arrow<T>, val isConstant: Boolean)

val <T> Arrow<T>.constantBinding get() = Binding(this, isConstant = true)
val <T> Arrow<T>.lambdaBinding get() = Binding(this, isConstant = false)

fun <V, T> Binding<T>.apply(index: Int, typed: Typed<V, T>): Typed<V, T>? =
	ifOrNull(arrow.givenType == typed.type) {
		applyTerm(index, typed.term)?.of(arrow.givesType)
	}

fun <V, T> Binding<T>.applyTerm(index: Int, param: Term<V>): Term<V>? =
	if (isConstant) get(index)
	else get<V>(index).invoke(param)
