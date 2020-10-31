package leo20

data class Function(val scope: Scope, val body: Body)

fun Scope.function(body: Body) = Function(this, body)

fun Function.apply(param: Value): Value = scope
	.push(param)
	.unsafeValue(body)

fun Function.applyRecursively(pattern: Pattern, param: Value): Value = scope
	.push(Definition(pattern, this, isRecursive = true))
	.push(param)
	.unsafeValue(body)