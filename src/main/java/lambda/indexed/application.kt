package lambda.indexed

data class Application(val lhs: Term, val rhs: Term) {
	override fun toString() = string
}

fun application(lhs: Term, rhs: Term) = Application(lhs, rhs)
