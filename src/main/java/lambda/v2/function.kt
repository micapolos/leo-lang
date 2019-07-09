package lambda.v2

data class Function(val term: Term) {
	override fun toString() = string(0)
}

fun function(term: Term) = Function(term)
