package leo15.lambda.runtime.builder.type

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.*

data class Scope<T>(
	val bindingStack: Stack<Binding<T>>,
	val typeStack: Stack<Type<T>>)

fun <T> emptyScope(): Scope<T> = Scope(stack(), stack())

operator fun <T> Scope<T>.plus(binding: Binding<T>) = copy(bindingStack = bindingStack.push(binding))
operator fun <T> Scope<T>.plus(type: Type<T>) = copy(typeStack = typeStack.push(type))

fun <V, T> Scope<T>.cast(typed: Typed<V, T>): Typed<V, T> =
	typeStack.mapFirst { typed.castTo(this) } ?: typed

fun <V, T> Scope<T>.applyBinding(typed: Typed<V, T>): Typed<V, T> =
	bindingStack.seq.indexed
		.mapFirstOrNull { value.apply(index, typed) }
		?: typed

fun <V, T> Scope<T>.apply(typed: Typed<V, T>): Typed<V, T> =
	applyBinding(cast(typed))
