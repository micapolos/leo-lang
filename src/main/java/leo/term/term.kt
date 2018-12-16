package leo.term

import leo.Word
import leo.base.fold
import leo.base.ifNull

sealed class Term<out V>

data class ValueTerm<out V>(
	val value: V) : Term<V>() {
	override fun toString() = "$value"
}

data class ApplicationTerm<out V>(
	val receiver: Receiver<V>,
	val application: Application<V>) : Term<V>() {
	override fun toString() = "$receiver$application"
}

val <V> V.term: Term<V>
	get() =
		ValueTerm(this)

fun <V> term(value: V): Term<V> =
	ValueTerm(value)

fun <V> term(word: Word): Term<V> =
	term(word apply null)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Term<V> =
	ApplicationTerm(receiver, word apply termOrNull)

fun <V> Term<V>?.apply(application: Application<V>): Term<V> =
	ApplicationTerm(receiver, application)

fun <V> term(term: Term<V>, vararg applications: Application<V>): Term<V> =
	term.fold(applications, Term<V>::apply)

fun <V> term(application: Application<V>, vararg applications: Application<V>): Term<V> =
	term(application.term, *applications)

val Term<*>.isSimple: Boolean
	get() =
		when (this) {
			is ValueTerm -> true
			is ApplicationTerm -> receiver.termOrNull == null
		}

val <V> Term<V>.applicationTermOrNull: ApplicationTerm<V>?
	get() =
		this as? ApplicationTerm

// === matching ===

fun <V, R : Any> Term<V>.matchPartial(word: Word, fn: Term<V>?.(Term<V>?) -> R?): R? =
	applicationTermOrNull?.let { applicationTerm ->
		applicationTerm.application.match(word) { termOrNull ->
			applicationTerm.receiver.termOrNull.fn(termOrNull)
		}
	}

fun <V, R : Any> Term<V>.match(word: Word, fn: (Term<V>?) -> R?): R? =
	matchPartial(word) { termOrNull ->
		ifNull {
			fn(termOrNull)
		}
	}

fun <V, R : Any> Term<V>.match(word1: Word, word2: Word, fn: (Term<V>?, Term<V>?) -> R?): R? =
	matchPartial(word2) { termOrNull2 ->
		this?.match(word1) { termOrNull1 ->
			fn(termOrNull1, termOrNull2)
		}
	}
