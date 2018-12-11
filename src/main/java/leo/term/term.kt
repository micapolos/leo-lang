package leo.term

import leo.*
import leo.base.appendableString
import leo.base.fold
import leo.base.nullOf

sealed class Term<out V>

data class MetaTerm<out V>(
	val meta: Meta<V>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class OperatorTerm<out V>(
	val operator: Operator<V>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Meta<V>.term: Term<V>
	get() =
		MetaTerm(this)

val <V> Operator<V>.onlyTerm: Term<V>
	get() =
		OperatorTerm(this)

fun <V> Term<V>?.plus(word: Word, rightTermOrNull: Term<V>? = null): Term<V> =
	apply(word, rightTermOrNull).onlyTerm

fun <V> term(word: Word, rightTermOrNull: Term<V>? = null): Term<V> =
	nullOf<Term<V>>().plus(word, rightTermOrNull)

fun <V> Word.term(): Term<V> =
	term(this)

val Word.term: Term<Nothing>
	get() =
		term()

fun <V> metaTerm(value: V): Term<V> =
	value.meta.term

val <V> Application<V>.term: Term<V>
	get() =
		operator.onlyTerm

fun <V> Term<V>?.plus(application: Application<V>): Term<V> =
	plus(application.word, application.termOrNull)

fun <V> term(application: Application<V>, vararg applications: Application<V>): Term<V> =
	term(application.term, *applications)

fun <V> term(term: Term<V>, vararg applications: Application<V>): Term<V> =
	term.fold(applications, Term<V>::plus)

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? MetaTerm

val <V> Term<V>.operatorTermOrNull
	get() =
		this as? OperatorTerm

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		when (this) {
			is MetaTerm -> true
			is OperatorTerm -> operator.termOrNull == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is MetaTerm -> append(term.meta)
		is OperatorTerm -> append(term.operator)
	}

// === map

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	when (this) {
		is MetaTerm -> meta.map(fn).term
		is OperatorTerm -> operator.map(fn).onlyTerm
	}
