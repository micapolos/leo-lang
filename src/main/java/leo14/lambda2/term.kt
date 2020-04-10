package leo14.lambda2

sealed class Term

data class ValueTerm(val value: Any?) : Term()
data class AbstractionTerm(val body: Term) : Term()
data class ApplicationTerm(val lhs: Term, val rhs: Term) : Term()
data class IndexTerm(val index: Int) : Term()

fun term(value: Any?): Term = ValueTerm(value)
fun fn(fn: (Term) -> Term): Term = term(fn)
fun fn(body: Term): Term = AbstractionTerm(body)
operator fun Term.invoke(rhs: Term): Term = ApplicationTerm(this, rhs)
fun at(index: Int): Term = IndexTerm(index)

val Term.value: Any? get() = (this as ValueTerm).value
