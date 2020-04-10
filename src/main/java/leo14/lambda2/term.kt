package leo14.lambda2

sealed class Term {
	override fun toString() = print
}

data class ValueTerm(val value: Any?) : Term() {
	override fun toString() = super.toString()
}

data class AbstractionTerm(val body: Term) : Term() {
	override fun toString() = super.toString()
}

data class ApplicationTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class IndexTerm(val index: Int) : Term() {
	override fun toString() = super.toString()
}

fun value(value: Any?): Term = ValueTerm(value)
fun fn(fn: (Term) -> Term): Term = value(fn)
fun fn(body: Term): Term = AbstractionTerm(body)
operator fun Term.invoke(rhs: Term): Term = ApplicationTerm(this, rhs)
fun at(index: Int): Term = IndexTerm(index)

val Term.value: Any? get() = (this as ValueTerm).value
