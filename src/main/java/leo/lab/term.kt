package leo.lab

import leo.*
import leo.base.*

sealed class Term<out V> {
	data class Meta<V>(
		val value: V) : Term<V>()

	data class Structure<out V>(
		val fieldStack: Stack<Field<V>>) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}
}

// === constructors

val <V> V.metaTerm
	get() =
		Term.Meta(this)

val <V> Stack<Field<V>>.term
	get() =
		Term.Structure(this)

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).term

fun <V> Word.term(): Term<V> =
	term(field())

val Word.term: Term<Nothing>
	get() =
		term()

val <V> Field<V>.term: Term<V>
	get() =
		onlyStack.term

// === casting

val <V> Term<V>.metaTermOrNull
	get() =
		this as? Term.Meta

val <V> Term<V>.structureTermOrNull
	get() =
		this as? Term.Structure

// === Appendable (pretty-print)

val <V> Term<V>.isSimple: Boolean
	get() =
		when (this) {
			is Term.Meta -> true
			is Term.Structure -> this.fieldStack.pop == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is Term.Meta -> append(term.value.string)
		is Term.Structure -> term.fieldStack.reverse.stream
			.foldFirst { field -> append(field) }
			.foldNext { field -> append(", ").append(field) }
	}

// === byte stream

val Term<Nothing>.coreString: String
	get() =
		appendableString { appendable ->
			appendable.fold(byteStream) { byte ->
				append(byte.toChar())
			}
		}

val <V> Term<V>.byteStream: Stream<Byte>
	get() =
		structureTermOrNull?.let {
			it.fieldStack.reverse.stream
				.foldFirst { field -> field.byteStream }
				.foldNext { field -> then(field.byteStream) }
		} ?: fail

// === access

fun <V> Term<V>.all(key: Word): Stack<Term<V>?>? =
	structureTermOrNull?.run {
		fieldStack.filterMap { field -> field.get(key) }
	}

fun <V> Term<V>.only(key: Word): The<Term<V>?>? =
	all(key)?.theOnlyOrNull

fun <V> Term<V>.theOnly(key: Word): The<Term<V>?>? =
	structureTermOrNull?.run {
		when {
			fieldStack.pop != null -> null
			fieldStack.top.word != key -> null
			else -> fieldStack.top.termOrNull.the
		}
	}

val <V> Term<V>.onlyFieldOrNull: Field<V>?
	get() =
		structureTermOrNull?.run { fieldStack.theOnlyOrNull?.value }

fun <V, R> Term<V>.match(key: Word, fn: (Term<V>?) -> R): R? =
	structureTermOrNull?.run {
		when {
			fieldStack.top.word != key -> null
			fieldStack.pop != null -> null
			else -> fn(fieldStack.top.termOrNull)
		}
	}

fun <V, R> Term<V>.match(firstKey: Word, secondKey: Word, fn: (Term<V>?, Term<V>?) -> R): R? =
	structureTermOrNull?.run {
		when {
			fieldStack.top.word != secondKey -> null
			fieldStack.pop?.top?.word != firstKey -> null
			fieldStack.pop.pop != null -> null
			else -> fn(fieldStack.pop.top.termOrNull, fieldStack.top.termOrNull)
		}
	}

fun <V> Term<V>.select(key: Word): The<Term<V>?>? =
	all(key)?.run {
		when {
			pop == null -> top
			else -> reverse.stream
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

val Term<Nothing>.reflect: Field<Nothing>
	get() = reflect { fail }

fun <V> Term<V>.reflect(metaReflect: V.() -> Field<Nothing>): Field<Nothing> =
		when (this) {
			is Term.Meta -> metaWord fieldTo term(metaReflect(value))
			is Term.Structure -> termWord fieldTo fieldStack.reflect { field -> field.reflect(metaReflect) }
		}

// === pushing

fun <V> Term<V>?.push(word: Word): Term<V>? =
	if (this == null) word.term
	else this.structureTermOrNull?.fieldStack?.push(word.field)?.term

fun <V> Term<V>?.push(field: Field<V>): Term<V>? =
	if (this == null) field.term
	else this.structureTermOrNull?.fieldStack?.push(field)?.term
