package leo.term

import leo.Word
import leo.append
import leo.base.ifNotNull

data class Application<out V>(
	val word: Word,
	val termOrNull: Term<V>?)

infix fun <V> Word.apply(termOrNull: Term<V>?): Application<V> =
	Application(this, termOrNull)

fun <V> Appendable.append(application: Application<V>): Appendable =
	this
		.append(application.word)
		.ifNotNull(application.termOrNull) { term ->
			if (term.isSimple) append(' ').append(term)
			else append('(').append(term).append(')')
		}

fun <V, R> Application<V>.map(fn: V.() -> R): Application<R> =
	word apply termOrNull?.map(fn)