package leo.term

import leo.Word
import leo.base.ifNotNull
import leo.base.nullOf

data class Application<out V>(
	val termOrNull: Term<V>?,
	val line: Line<V>)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Application<V> =
	Application(this, word lineTo termOrNull)

val <V> Line<V>.application: Application<V>
	get() =
		nullOf<Term<V>>().apply(word, termOrNull)

fun <V> Application<V>.plus(line: Line<V>): Application<V> =
	Application(onlyTerm, line)

fun <V> Appendable.append(application: Application<V>): Appendable =
	this
		.ifNotNull(application.termOrNull) { term ->
			append(term).append(", ")
		}
		.append(application.line)

fun <V, R> Application<V>.map(fn: V.() -> R): Application<R> =
	Application(termOrNull?.map(fn), line.map(fn))