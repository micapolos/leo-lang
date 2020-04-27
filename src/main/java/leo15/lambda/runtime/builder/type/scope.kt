package leo15.lambda.runtime.builder.type

import leo13.Stack
import leo13.push
import leo13.stack

data class Scope<T>(
	val bindingStack: Stack<Binding<T>>,
	val typeStack: Stack<Type<T>>)

fun <T> emptyScope(): Scope<T> = Scope(stack(), stack())

operator fun <T> Scope<T>.plus(binding: Binding<T>) = copy(bindingStack = bindingStack.push(binding))
operator fun <T> Scope<T>.plus(type: Type<T>) = copy(typeStack = typeStack.push(type))
