package leo.term

import leo.Word
import leo.append
import leo.base.ifNotNull

// TODO: Better name!!!
data class Line<out V>(
	val word: Word,
	val termOrNull: Term<V>?)

infix fun <V> Word.lineTo(termOrNull: Term<V>?): Line<V> =
	Line(this, termOrNull)

fun <V> Appendable.append(line: Line<V>): Appendable =
	this
		.append(line.word)
		.ifNotNull(line.termOrNull) { term ->
			if (term.isSimple) append(' ').append(term)
			else append('(').append(term).append(')')
		}

fun <V, R> Line<V>.map(fn: V.() -> R): Line<R> =
	word lineTo termOrNull?.map(fn)