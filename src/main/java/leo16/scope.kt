package leo16

import leo13.Stack
import leo13.mapFirst
import leo13.push
import leo13.stack

data class Scope(val bindingStack: Stack<Binding>)

val Stack<Binding>.scope get() = Scope(this)
val emptyScope = stack<Binding>().scope
operator fun Scope.plus(binding: Binding): Scope = bindingStack.push(binding).scope

fun Scope.apply(value: Value): Value? =
	bindingStack.mapFirst { apply(value) }

fun Scope.compile(script: Script): Compiled? =
	compiler.plus(script)?.compiled

fun Scope.evaluate(script: Script): Value? =
	compile(script)?.value
