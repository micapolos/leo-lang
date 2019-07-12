package lambda.script

import lambda.Application

sealed class Term

data class VariableTerm(val variable: Variable) : Term() {
	override fun toString() = "$variable"
}

data class ApplicationTerm(val application: Application) : Term()
data class FunctionTerm(val abstraction: Function) : Term()

fun term(variable: Variable): Term = VariableTerm(variable)
fun term(application: Application): Term = ApplicationTerm(application)
fun term(function: Function): Term = FunctionTerm(function)
