package leo15.lambda

import leo15.string

sealed class Term {
	override fun toString() = script.string
}

data class ValueTerm(val value: Any?) : Term() {
	override fun toString() = super.toString()
}

data class AbstractionTerm(val body: Term, val isRepeating: Boolean) : Term() {
	override fun toString() = super.toString()
}

data class ApplicationTerm(val lhs: Term, val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

data class IndexTerm(val index: Int) : Term() {
	override fun toString() = super.toString()
}

data class RepeatTerm(val rhs: Term) : Term() {
	override fun toString() = super.toString()
}

val Any?.valueTerm: Term get() = ValueTerm(this)
fun value(value: Any?): Term = ValueTerm(value)
fun valueFn(fn: (Any?) -> Any?): Term = termFn { fn(it.value).valueTerm }
fun termFn(fn: (Term) -> Term?): Term = thunkFn { fn(it.term)?.thunk }
fun thunkFn(fn: (Thunk) -> Thunk?): Term = value(fn)
fun fn(body: Term, isRepeating: Boolean): Term = AbstractionTerm(body, isRepeating)
fun fn(body: Term): Term = fn(body, isRepeating = false)
fun repeating(body: Term): Term = fn(body, isRepeating = true)
operator fun Term.invoke(rhs: Term): Term = ApplicationTerm(this, rhs)
fun at(index: Int): Term = IndexTerm(index)
val Term.repeat: Term get() = RepeatTerm(this)

val Term.value: Any? get() = (this as ValueTerm).value
val Term.applicationOrNull: ApplicationTerm? get() = (this as? ApplicationTerm)
val Term.abstractionOrNull: AbstractionTerm? get() = (this as? AbstractionTerm)
val Term.indexOrNull: Int? get() = (this as? IndexTerm)?.index
