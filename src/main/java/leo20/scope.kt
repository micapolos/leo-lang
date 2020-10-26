package leo20

import leo.base.notNullIf
import leo.base.runIf
import leo13.Stack
import leo13.fold
import leo13.mapFirst
import leo13.push
import leo13.reverse
import leo13.stack

data class Scope(val bindingStack: Stack<Binding>)

data class Binding(val pattern: Pattern, val value: Value, val isFunction: Boolean)

val Line.binding get() = Binding(pattern(selectName fieldTo pattern()), value(this), false)

val emptyScope = Scope(stack())
fun Scope.push(binding: Binding): Scope = Scope(bindingStack.push(binding))
fun Scope.push(param: Value): Scope = fold(param.lineStack.reverse) { push(it.binding) }

fun Scope.resolveOrNull(param: Value): Value? =
	bindingStack.mapFirst { resolveOrNull(param) }

fun Binding.resolveOrNull(param: Value): Value? =
	notNullIf(param.matches(pattern)) {
		value.runIf(isFunction) { apply(param) }
	}
