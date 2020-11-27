package leo23.term.eval

import leo.base.fold
import leo.stak.push
import leo23.term.Expr
import leo23.value.Value

data class Fn(val scope: Scope, val body: Expr)

fun Fn.apply(vararg params: Value): Value =
	apply(params.asIterable())

fun Fn.apply(params: Iterable<Value>): Value =
	scope.fold(params, Scope::push).eval(body)

fun Fn.applyRecursive(vararg params: Value): Value =
	applyRecursive(params.asIterable())

fun Fn.applyRecursive(params: Iterable<Value>): Value =
	scope.push(this).fold(params, Scope::push).eval(body)

