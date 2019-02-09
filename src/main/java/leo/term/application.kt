package leo.term

import leo.Word
import leo.base.fold
import leo.base.nullOf

data class Application<out V>(
	val word: Word,
	val parameter: Parameter<V>) {
	override fun toString() = "$word$parameter"
}

fun <V> Word.application(vararg applications: Application<V>): Application<V> =
	apply(nullOf<Term<V>>().fold(applications) { apply(it) })

infix fun <V> Word.apply(termOrNull: Term<V>?): Application<V> =
	Application(this, termOrNull.parameter)

infix fun <V> Word.applyValue(value: V): Application<V> =
	apply(valueTerm(value))

val <V> Application<V>.fieldOrNull: Field<V>?
	get() =
		parameter.termOrNull?.let { term -> word fieldTo term }

val <V> Application<V>.term: Term<V>
	get() =
		ApplicationTerm(subject(null), this)

// === matching

fun <V, R : Any> Application<V>.isMatching(word: Word, fn: (Term<V>?) -> R?): R? =
	if (this.word == word) fn(parameter.termOrNull)
	else null