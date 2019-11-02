package leo13.js.compiler

data class Lambda(val name: String, val body: Expression)

fun lambda(name: String, body: Expression) = Lambda(name, body)

val Lambda.code
	get() =
		"function($name) { ${body.returnCode} }"