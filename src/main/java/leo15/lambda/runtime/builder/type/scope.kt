package leo15.lambda.runtime.builder.type

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.*

data class Scope<V, T>(
	val bindingStack: Stack<Binding<V, T>>,
	val typeStack: Stack<Type<T>>)

fun <V, T> emptyScope(): Scope<V, T> = Scope(stack(), stack())

operator fun <V, T> Scope<V, T>.plus(binding: Binding<V, T>) = copy(bindingStack = bindingStack.push(binding))
operator fun <V, T> Scope<V, T>.plus(type: Type<T>) = copy(typeStack = typeStack.push(type))

fun <V, T> Scope<V, T>.cast(typed: Typed<V, T>): Typed<V, T> =
	typeStack.mapFirst { typed.castTo(this) } ?: typed

fun <V, T> Scope<V, T>.applyBinding(typed: Typed<V, T>): Typed<V, T> =
	bindingStack.seq.indexed
		.mapFirstOrNull { value.apply(index, typed) }
		?: typed

fun <V, T> Scope<V, T>.apply(typed: Typed<V, T>): Typed<V, T> =
	applyBinding(cast(typed))
