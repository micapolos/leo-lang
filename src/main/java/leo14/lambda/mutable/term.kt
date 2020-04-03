package leo14.lambda.mutable

sealed class Term<out T>
data class ValueTerm<T>(val value: T) : Term<T>()
data class AbstractionTerm<T>(val variable: Variable<T>, val body: Term<T>) : Term<T>()
data class ApplicationTerm<T>(val lhs: Term<T>, val rhs: Term<T>) : Term<T>()
data class VariableTerm<T>(val variable: Variable<T>) : Term<T>()
data class Variable<T>(var term: Term<T>?)

fun <T> term(value: T): Term<T> = ValueTerm(value)
fun <T> term(variable: Variable<T>, body: Term<T>): Term<T> = AbstractionTerm(variable, body)
fun <T> term(lhs: Term<T>, rhs: Term<T>): Term<T> = ApplicationTerm(lhs, rhs)
fun <T> term(variable: Variable<T>): Term<T> = VariableTerm(variable)
fun <T> variable(term: Term<T>? = null) = Variable(term)

fun <T> term(fn: (Variable<T>) -> Term<T>): Term<T> =
	variable<T>().let { variable ->
		term(variable, fn(variable))
	}

val <T> Term<T>.apply: Term<T>
	get() =
		when (this) {
			is ValueTerm -> this
			is AbstractionTerm -> this
			is ApplicationTerm -> (lhs as? AbstractionTerm)?.apply(rhs) ?: this
			is VariableTerm -> variable.term!!
		}

fun <T> AbstractionTerm<T>.apply(term: Term<T>): Term<T> {
	variable.term = term
	try {
		return body.apply
	} finally {
		variable.term = null
	}
}

