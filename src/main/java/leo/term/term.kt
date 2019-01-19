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
	val subject: Subject<V>,
	val application: Application<V>) : Term<V>() {
	override fun toString() = "$subject$application"
}

fun <V> valueTerm(value: V): Term<V> =
	ValueTerm(value)

fun <V> term(word: Word): Term<V> =
	term(word apply null)

fun <V> Term<V>?.apply(word: Word, termOrNull: Term<V>?): Term<V> =
	ApplicationTerm(subject, word apply termOrNull)

fun <V> Term<V>?.apply(application: Application<V>): Term<V> =
	ApplicationTerm(subject, application)

fun <V> term(term: Term<V>, vararg applications: Application<V>): Term<V> =
	term.fold(applications, Term<V>::apply)

fun <V> term(application: Application<V>, vararg applications: Application<V>): Term<V> =
	term(application.term, *applications)

val Term<*>.isSimple: Boolean
	get() =
		when (this) {
			is ValueTerm -> true
			is ApplicationTerm -> subject.termOrNull == null
		}

val <V> Term<V>.applicationTermOrNull: ApplicationTerm<V>?
	get() =
		this as? ApplicationTerm

fun <V, R> R.foldApplicationsOrNull(term: Term<V>, fn: R.(Application<V>?) -> R): R =
	when (term) {
		is ValueTerm -> fn(null)
		is ApplicationTerm ->
			if (term.subject.termOrNull == null) this
			else fn(term.application).foldApplicationsOrNull(term.subject.termOrNull, fn)
	}

// === matching ===

fun <V, R : Any> Term<V>.matchPartial(word: Word, fn: Term<V>?.(Term<V>?) -> R?): R? =
	applicationTermOrNull?.let { applicationTerm ->
		applicationTerm.application.isMatching(word) { termOrNull ->
			applicationTerm.subject.termOrNull.fn(termOrNull)
		}
	}

fun <V, R : Any> Term<V>.isMatching(word: Word, fn: (Term<V>?) -> R?): R? =
	matchPartial(word) { termOrNull ->
		ifNull {
			fn(termOrNull)
		}
	}

fun <V, R : Any> Term<V>.isMatching(word1: Word, word2: Word, fn: (Term<V>?, Term<V>?) -> R?): R? =
	matchPartial(word2) { termOrNull2 ->
		this?.isMatching(word1) { termOrNull1 ->
			fn(termOrNull1, termOrNull2)
		}
	}
