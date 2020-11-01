package leo21.compiled

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.fold
import leo13.reverse
import leo13.seq
import leo21.typed.Typed

data class Scope(val bindingStack: Stack<Binding>)

fun Scope.resolveOrNull(name: String): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, name)
	}

fun Scope.resolveOrNull(typed: Typed): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, typed)
	}

fun Scope.resolve(typed: Typed): Typed =
	resolveOrNull(typed) ?: typed

fun Typed.push(scope: Scope): Typed =
	fold(scope.bindingStack.reverse) { push(it) }