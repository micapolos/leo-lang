package lambda.script

sealed class Term

data class VariableTerm(val variable: Variable) : Term() {
	override fun toString() = "$variable"
}

data class ApplicationTerm(val application: Application) : Term() {
	override fun toString() = "$application"
}

data class FunctionTerm(val function: Function) : Term() {
	override fun toString() = "$function"
}

fun term(variable: Variable): Term = VariableTerm(variable)
fun term(application: Application): Term = ApplicationTerm(application)
fun term(function: Function): Term = FunctionTerm(function)
