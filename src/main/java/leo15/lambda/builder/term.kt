package leo15.lambda.builder

import leo15.string

sealed class Term<out V> {
	override fun toString() = script(anyScriptFn).string
}

data class ValueTerm<out V>(val value: V) : Term<V>() {
	override fun toString() = super.toString()
}

data class AbstractionTerm<out V>(val body: Term<V>) : Term<V>() {
	override fun toString() = super.toString()
}

data class ApplicationTerm<out V>(val lhs: Term<V>, val rhs: Term<V>) : Term<V>() {
	override fun toString() = super.toString()
}

data class IndexTerm<out V>(val index: Int) : Term<V>() {
	override fun toString() = super.toString()
}

val <V> V.term: Term<V> get() = ValueTerm(this)
fun <V> lambda(body: Term<V>): Term<V> = AbstractionTerm(body)
operator fun <V> Term<V>.invoke(rhs: Term<V>): Term<V> = ApplicationTerm(this, rhs)
fun <V> get(index: Int): Term<V> = IndexTerm(index)

val <V> Term<V>.value: V get() = (this as ValueTerm).value
val <V> Term<V>.applicationOrNull: ApplicationTerm<V>? get() = (this as? ApplicationTerm)
val <V> Term<V>.abstractionOrNull: AbstractionTerm<V>? get() = (this as? AbstractionTerm)
val <V> Term<V>.indexOrNull: Int? get() = (this as? IndexTerm)?.index
