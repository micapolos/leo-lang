package leo.term

import leo.Word
import leo.base.ifNotNull
import leo.base.nullOf

data class Operator<out V>(
	val termOrNull: Term<V>?,
	val application: Application<V>)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Operator<V> =
	Operator(this, word apply termOrNull)

val <V> Application<V>.operator: Operator<V>
	get() =
		nullOf<Term<V>>().apply(word, termOrNull)

fun <V> Operator<V>.plus(application: Application<V>): Operator<V> =
	Operator(onlyTerm, application)

fun <V> Appendable.append(operator: Operator<V>): Appendable =
	this
		.ifNotNull(operator.termOrNull) { term ->
			append(term).append(", ")
		}
		.append(operator.application)

fun <V, R> Operator<V>.map(fn: V.() -> R): Operator<R> =
	Operator(termOrNull?.map(fn), application.map(fn))