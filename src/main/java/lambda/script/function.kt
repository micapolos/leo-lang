package lambda.script

data class Function(val variable: Variable, val body: Term) {
	override fun toString() = "$variable -> $body"
}

fun function(variable: Variable, body: Term) = Function(variable, body)
