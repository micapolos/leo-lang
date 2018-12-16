package leo.term

import leo.Word

data class Application<out V>(
	val word: Word,
	val argument: Argument<V>) {
	override fun toString() = "$word$argument"
}

fun <V> Word.application(): Application<V> =
	Application(this, argument(null))

infix fun <V> Word.apply(termOrNull: Term<V>?): Application<V> =
	Application(this, termOrNull.argument)

val <V> Application<V>.fieldOrNull: Field<V>?
	get() =
		argument.termOrNull?.let { term -> word fieldTo term }

val <V> Application<V>.term: Term<V>
	get() =
		ApplicationTerm(receiver(null), this)

// === matching

fun <V, R : Any> Application<V>.match(word: Word, fn: (Term<V>?) -> R?): R? =
	if (this.word == word) fn(argument.termOrNull)
	else null