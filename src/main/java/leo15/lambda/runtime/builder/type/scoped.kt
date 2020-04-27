package leo15.lambda.runtime.builder.type

import leo13.linkOrNull
import leo15.lambda.runtime.builder.invoke
import leo15.lambda.runtime.builder.lambda

data class Scoped<V, T>(val scope: Scope<V, T>, val typed: Typed<V, T>)

infix fun <V, T> Scope<V, T>.scoped(typed: Typed<V, T>) = Scoped(this, typed)

val <V, T> Scoped<V, T>.popAndBind: Scoped<V, T>?
	get() =
		scope.bindingStack.linkOrNull?.let { bindingLink ->
			scope.copy(bindingStack = bindingLink.stack)
				.scoped(typed.copy(term = lambda(typed.term).invoke(bindingLink.value.bound.typed.term)))
		}
