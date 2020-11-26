package leo23.term.eval

import leo.stak.push
import leo23.term.Term
import leo23.value.Value

data class Fn(val scope: Scope, val body: Term)

fun Fn.apply(vararg params: Value): Value =
	params.fold(scope) { acc, value -> acc.push(value) }.eval(body)

fun Fn.apply(params: List<Value>): Value =
	params.fold(scope) { acc, value -> acc.push(value) }.eval(body)

