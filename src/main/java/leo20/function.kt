package leo20

data class Function(val dictionary: Dictionary, val body: Body)

fun Dictionary.function(body: Body) = Function(this, body)

fun Function.apply(param: Value): Value = dictionary
	.push(param)
	.unsafeValue(body)

fun Function.applyRecursively(pattern: Pattern, param: Value): Value = dictionary
	.push(FunctionDefinition(pattern, this, isRecursive = true))
	.push(param)
	.unsafeValue(body)
