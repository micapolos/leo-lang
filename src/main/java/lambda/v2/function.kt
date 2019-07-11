package lambda.v2

data class Function(val body: Body) {
	override fun toString() = string
}

fun function(body: Body) = Function(body)
