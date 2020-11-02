package leo21.compiled

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.fold
import leo13.push
import leo13.reverse
import leo13.seq
import leo13.stack
import leo21.typed.Typed
import leo21.typed.resolve

data class Scope(val bindingStack: Stack<Binding>)

val emptyScope = Scope(stack())
fun Scope.push(binding: Binding) = Scope(bindingStack.push(binding))

fun Scope.resolveOrNull(name: String): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, name)
	}

fun Scope.resolveOrNull(typed: Typed): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, typed)
	}

fun Scope.resolve(typed: Typed): Typed =
	resolveOrNull(typed) ?: typed.resolve

fun Typed.push(scope: Scope): Typed =
	fold(scope.bindingStack.reverse) { push(it) }