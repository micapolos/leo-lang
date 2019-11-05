package leo14.lambda

import leo13.Index

sealed class Term<out T>

data class NativeTerm<T>(val native: T) : Term<T>()
data class AbstractionTerm<T>(val abstraction: Abstraction<Term<T>>) : Term<T>()
data class ApplicationTerm<T>(val application: Application<Term<T>>) : Term<T>()
data class VariableTerm<T>(val variable: Variable<T>) : Term<T>()

fun <T> term(native: T): Term<T> = NativeTerm(native)
fun <T> term(abstraction: Abstraction<Term<T>>): Term<T> = AbstractionTerm(abstraction)
fun <T> term(application: Application<Term<T>>): Term<T> = ApplicationTerm(application)
fun <T> term(variable: Variable<T>): Term<T> = VariableTerm(variable)

// === matching

fun <T, R> Term<T>.abstraction(fn: (Term<T>) -> R): R =
	when (this) {
		is AbstractionTerm -> fn(abstraction.body)
		else -> error("abstraction expected")
	}

fun <T, R> Term<T>.application(fn: (Term<T>, Term<T>) -> R): R =
	when (this) {
		is ApplicationTerm -> fn(application.lhs, application.rhs)
		else -> error("application expected")
	}

fun <T, R> Term<T>.variable(fn: (Index) -> R): R =
	when (this) {
		is VariableTerm -> fn(variable.index)
		else -> error("variable expected")
	}

fun <T, R> Term<T>.native(fn: (T) -> R): R =
	when (this) {
		is NativeTerm -> fn(native)
		else -> error("native expected")
	}
