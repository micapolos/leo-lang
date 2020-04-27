package leo15.lambda.runtime.builder.type

import leo13.Stack
import leo13.mapFirst

data class Types<out T>(val stack: Stack<Type<T>>)

fun <V, T> Types<T>.cast(typed: Typed<V, T>): Typed<V, T> =
	stack.mapFirst { typed.castTo(this) } ?: typed
