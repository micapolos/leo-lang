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

data class ApplicationTerm<out V>(
	val application: Application<V>) : Term<V>() {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Meta<V>.term: Term<V>
	get() =
		MetaTerm(this)

val <V> Application<V>.term: Term<V>
	get() =
		ApplicationTerm(this)

fun <V> Term<V>?.plus(word: Word, rightTermOrNull: Term<V>? = null): Term<V> =
	apply(word, rightTermOrNull).term

fun <V> term(word: Word, rightTermOrNull: Term<V>? = null): Term<V> =
	nullOf<Term<V>>().plus(word, rightTermOrNull)

fun <V> Word.term(): Term<V> =
	term(this)

val Word.term: Term<Nothing>
	get() =
		term()

fun <V> metaTerm(value: V): Term<V> =
	value.meta.term

val <V> Line<V>.term: Term<V>
	get() =
		application.term

fun <V> Term<V>?.plus(line: Line<V>): Term<V> =
	plus(line.operator.word, line.right.termOrNull)

fun <V> term(line: Line<V>, vararg lines: Line<V>): Term<V> =
	term(line.term, *lines)

fun <V> term(term: Term<V>, vararg lines: Line<V>): Term<V> =
	term.fold(lines, Term<V>::plus)

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? MetaTerm

val <V> Term<V>.applicationTermOrNull
	get() =
		this as? ApplicationTerm

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		when (this) {
			is MetaTerm -> true
			is ApplicationTerm -> application.left.termOrNull == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is MetaTerm -> append(term.meta)
		is ApplicationTerm -> append(term.application)
	}

// === map

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	when (this) {
		is MetaTerm -> meta.map(fn).term
		is ApplicationTerm -> application.map(fn).term
	}

