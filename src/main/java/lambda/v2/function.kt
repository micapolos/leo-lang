package lambda.v2

data class Function(val body: Body) {
	override fun toString() = string(0)
}

fun function(body: Body) = Function(body)
