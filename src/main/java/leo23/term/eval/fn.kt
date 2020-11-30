package leo23.term.eval

import leo.base.fold
import leo.base.runIf
import leo.stak.push
import leo23.term.Expr
import leo23.value.Scope
import leo23.value.Value

data class Fn(val scope: Scope, val isRecursive: Boolean, val body: Expr)

fun Fn.apply(vararg params: Value): Value =
	apply(params.asIterable())

fun Fn.apply(params: Iterable<Value>): Value =
	scope.runIf(isRecursive) { push(this@apply) }.fold(params, Scope::push).value(body)

