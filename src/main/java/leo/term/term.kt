package leo.term

import leo.Word

data class Term<out V : Any>(
	val receiverOrNull: V?,
	val application: Application<V>)

fun <V : Any> Word.term(): Term<V> =
	application<V>().term

fun <V : Any> V?.apply(word: Word, argumentOrNull: V?): Term<V> =
	Term(this, word apply argumentOrNull)

fun <V : Any> V?.apply(application: Application<V>): Term<V> =
	Term(this, application)

val Term<*>.isSimple: Boolean
	get() =
		receiverOrNull == null