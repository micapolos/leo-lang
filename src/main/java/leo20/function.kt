package leo20

data class Function(val bindings: Bindings, val body: Body)

fun Bindings.function(body: Body) = Function(this, body)

fun Function.apply(param: Value): Value = bindings
	.push(param)
	.unsafeValue(body)

fun Function.applyRecursively(pattern: Pattern, param: Value): Value = bindings
	.push(FunctionBinding(pattern, this, isRecursive = true))
	.push(param)
	.unsafeValue(body)
