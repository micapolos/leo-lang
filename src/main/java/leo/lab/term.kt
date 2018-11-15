package leo.lab

import leo.Word
import leo.base.*
import leo.lastWord
import leo.previousWord
import leo.termWord

data class Term<out V>(
	val fieldStack: Stack<Field<V>>) {
	override fun toString() = appendableString { it.append(this) }
}

// === constructors

val <V> Stack<Field<V>>.term
	get() =
		Term(this)

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).term

val Word.term: Term<Unit>
	get() =
		term(field)

val <V> Field<V>.term: Term<V>
	get() =
		onlyStack.term

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		fieldStack.pop == null

fun <V> Appendable.append(term: Term<V>): Appendable =
	term.fieldStack.reverse.stream
		.foldFirst { field -> append(field) }
		.foldNext { field -> append(", ").append(field) }

// === byte stream

val <V> Term<V>.coreString: String
	get() =
		appendableString { appendable ->
			appendable.fold(byteStream) { byte ->
				append(byte.toChar())
			}
		}

val <V> Term<V>.byteStream: Stream<Byte>
	get() =
		fieldStack.reverse.stream
			.foldFirst { field -> field.byteStream }
			.foldNext { field -> then(field.byteStream) }

// === access

fun <V> Term<V>.all(key: Word): Stack<Term<V>?>? =
	fieldStack
		.filterMap { field -> field.get(key) }

fun <V> Term<V>.only(key: Word): The<Term<V>?>? =
	all(key)?.theOnlyOrNull

fun <V> Term<V>.theOnly(key: Word): The<Term<V>?>? =
	when {
		fieldStack.pop != null -> null
		fieldStack.top.word != key -> null
		else -> fieldStack.top.termOrNull.the
	}

val <V> Term<V>.onlyFieldOrNull: Field<V>?
	get() =
		fieldStack.theOnlyOrNull?.value

fun <V, R> Term<V>.match(key: Word, fn: (Term<V>?) -> R): R? =
	when {
		fieldStack.top.word != key -> null
		fieldStack.pop != null -> null
		else -> fn(fieldStack.top.termOrNull)
	}

fun <V, R> Term<V>.match(firstKey: Word, secondKey: Word, fn: (Term<V>?, Term<V>?) -> R): R? =
	when {
		fieldStack.top.word != secondKey -> null
		fieldStack.pop?.top?.word != firstKey -> null
		fieldStack.pop.pop != null -> null
		else -> fn(fieldStack.pop.top.termOrNull, fieldStack.top.termOrNull)
	}

fun Term<Unit>.select(key: Word): The<Term<Unit>?>? =
	all(key)?.let { termStack ->
		when {
			termStack.pop == null -> termStack.top
			else -> termStack.reverse.stream
				.foldFirst { term ->
					term(lastWord fieldTo term)
				}
				.foldNext { term ->
					term(
						previousWord fieldTo this,
						lastWord fieldTo term)
				}
		}.the
	}

// === reflect

val <V> Term<V>.reflect: Field<Unit>
	get() =
		termWord fieldTo fieldStack.reflect(Field<V>::reflect)

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	Term(fieldStack.map { field -> field.map(fn) })
