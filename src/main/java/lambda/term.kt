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

fun Term.apply(term: Term): Term = term(application(this, term))

val Term.eval
	get(): Term = when (this) {
		is ApplicationTerm -> application.left.eval.let { leftEval ->
			when (leftEval) {
				is FunctionTerm -> leftEval.function(application.right).eval
				else -> leftEval.apply(application.right.eval)
			}
		}
		else -> this
}

operator fun Term.invoke(term: Term): Term = apply(term).eval
operator fun Term.invoke(term: Term, vararg terms: Term): Term =
	invoke(term).fold(terms) { invoke(it) }

fun term(fn: (Term) -> Term): Term = FunctionTerm(function(fn))
fun term(fn: (Term, Term) -> Term): Term = term { t1 -> term { t2 -> fn(t1, t2) } }
fun term(fn: (Term, Term, Term) -> Term): Term = term { t1 -> term { t2 -> term { t3 -> fn(t1, t2, t3) } } }

fun Term.termLet(fn: (Term) -> Term) =
	term { x -> fn(x) }.invoke(this)

fun Term.termRun(fn: (Term) -> Unit) =
	termLet { it }

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