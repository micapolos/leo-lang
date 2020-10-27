package leo20

import leo.base.runIf

data class Function(val scope: Scope, val body: Body, val isRecursive: Boolean)

fun Scope.function(body: Body) = Function(this, body, isRecursive = false)

fun Function.apply(param: Value): Value = scope
	.runIf(isRecursive) {
		push(
			Binding(
				anyPattern.plus("recurse" lineTo pattern()),
				value(line(this@apply)),
				true))
	}
	.push(param)
	.unsafeValue(body)
