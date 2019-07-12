package lambda.script

data class Variable(val string: String) {
	override fun toString() = string
}

fun variable(string: String) = Variable(string)
