package lambda

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

val Term.applicationOrNull get() = (this as? ApplicationTerm)?.application
val Term.functionOrNull get() = (this as? FunctionTerm)?.function

fun Term.apply(term: Term): Term = ApplicationTerm(application(this, term)).reduce
fun term(fn: (Term) -> Term): Term = FunctionTerm(function(fn))

val Term.reduce: Term get() = applicationOrNull?.reduce ?: this
fun Term.invoke(term: Term) = functionOrNull?.invoke(term) ?: this

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