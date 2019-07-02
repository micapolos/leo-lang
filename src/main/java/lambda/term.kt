package lambda

import leo.base.fold

sealed class Term {
	override fun toString() = code
	override fun equals(other: Any?) = other is Term && eq(other)
}

class VariableTerm(val variable: Variable) : Term()
class ApplicationTerm(val application: Application) : Term()
class FunctionTerm(val function: Function) : Term()

fun term(variable: Variable): Term = VariableTerm(variable)
fun term(application: Application): Term = ApplicationTerm(application)
fun term(function: Function): Term = FunctionTerm(function)

operator fun Term.invoke(term: Term): Term = when (this) {
	is VariableTerm -> this
	is ApplicationTerm -> term(application(this, term))
	is FunctionTerm -> function(term)
}

operator fun Term.invoke(term: Term, vararg terms: Term): Term =
	invoke(term).fold(terms) { invoke(it) }

fun term(fn: (Term) -> Term): Term = FunctionTerm(function(fn))

val Term.code: String
	get() = when (this) {
		is VariableTerm -> variable.code
		is ApplicationTerm -> application.code
		is FunctionTerm -> function.code
	}

fun Term.eq(term: Term): Boolean = when (this) {
	is VariableTerm -> (term is VariableTerm) && variable.eq(term.variable)
	is ApplicationTerm -> (term is ApplicationTerm) && application.eq(term.application)
	is FunctionTerm -> (term is FunctionTerm) && function.eq(term.function)
}