package leo

import leo.base.*

sealed class Term<out V> {
	data class Meta<V>(
		val value: V) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}

	data class Structure<out V>(
		val lhsTermOrNull: Term<V>?,
		val word: Word,
		val rhsTermOrNull: Term<V>?) : Term<V>() {
		override fun toString() = appendableString { it.append(this) }
	}
}

// === constructors

val <V> V.metaTerm
	get() =
		Term.Meta(this)

val <V> Stack<Field<V>>.term
	get() =
		reverse.stream
			.foldFirst(Field<V>::term)
			.foldNext(Term<V>::push)

fun <V> term(field: Field<V>, vararg fields: Field<V>) =
	stack(field, *fields).term

fun <V> Word.term(): Term<V> =
	term(field())

val Word.term: Term<Nothing>
	get() =
		term()

val <V> Field<V>.term: Term<V>
	get() =
		Term.Structure(null, word, termOrNull)

fun <V> Term<V>?.push(word: Word): Term<V> =
	push(word.field)

fun <V> Term<V>?.push(field: Field<V>): Term<V> =
	Term.Structure(this, field.word, field.termOrNull)

val <V> Term<V>.topFieldOrNull: Field<V>?
	get() =
		structureTermOrNull?.topField

val <V> Term.Structure<V>.topField: Field<V>
	get() =
		word fieldTo rhsTermOrNull

// === fields

val <V> Term<V>.fieldStreamOrNull: Stream<Field<V>>?
	get() =
		structureTermOrNull?.fieldStream

val <V> Term.Structure<V>.fieldStream: Stream<Field<V>>
	get() =
		Stream(word.fieldTo(rhsTermOrNull)) {
			lhsTermOrNull?.fieldStreamOrNull
		}

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
			is Term.Structure -> this.lhsTermOrNull == null
		}

fun <V> Appendable.append(term: Term<V>): Appendable =
	when (term) {
		is Term.Meta -> append(term.value.string)
		is Term.Structure -> term.fieldStream.reverse
			.foldFirst { field -> append(field) }
			.foldNext { field -> append(", ").append(field) }
	}

// === byte stream

val Term<Nothing>.coreString: String
	get() =
		appendableString { appendable ->
			appendable.fold(bitStream.bitByteStreamOrNull) { byte ->
				append(byte.toChar())
			}
		}

val Term<Nothing>.bitStream: Stream<Bit>
	get() =
		structureTermOrNull?.let {
			it.fieldStream.reverse
				.foldFirst { field -> field.bitStream }
				.foldNext { field -> then(field.bitStream) }
		} ?: fail

// === access

fun <V> Term<V>.all(key: Word): Stack<Term<V>?>? =
	structureTermOrNull?.run {
		fieldStream.stack.reverse.filterMap { field -> field.get(key) }
	}

fun <V> Term<V>.only(key: Word): The<Term<V>?>? =
	all(key)?.theOnlyOrNull

//fun <V> Term<V>.theOnly(key: Word): The<Term<V>?>? =
//	structureTermOrNull?.run {
//		when {
//			fieldStack.pop != null -> null
//			fieldStack.top.word != key -> null
//			else -> fieldStack.top.termOrNull.the
//		}
//	}

val <V> Term<V>.onlyFieldOrNull: Field<V>?
	get() =
		structureTermOrNull?.run {
			if (lhsTermOrNull != null) null
			else word fieldTo rhsTermOrNull
		}

fun <V, R> Term<V>.match(key: Word, fn: (Term<V>?) -> R): R? =
	structureTermOrNull?.run {
		when {
			word != key -> null
			lhsTermOrNull != null -> null
			else -> fn(rhsTermOrNull)
		}
	}

fun <V, R> Term<V>.match(firstKey: Word, secondKey: Word, fn: (Term<V>?, Term<V>?) -> R): R? =
	structureTermOrNull?.run {
		when {
			word != secondKey -> null
			lhsTermOrNull == null -> null
			lhsTermOrNull.structureTermOrNull?.word != firstKey -> null
			lhsTermOrNull.structureTermOrNull?.lhsTermOrNull != null -> null
			else -> fn(lhsTermOrNull.structureTermOrNull?.rhsTermOrNull, rhsTermOrNull)
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
		is Term.Structure -> termWord fieldTo this.fieldStream.reflect { reflect(metaReflect) }
	}

// === select

val Term<Nothing>.select: Term<Nothing>?
	get() =
		when (this) {
			is Term.Meta -> this
			is Term.Structure ->
				when {
					rhsTermOrNull != null -> this
					lhsTermOrNull == null -> this
					else -> lhsTermOrNull.select(word)?.value ?: word.fieldTo(lhsTermOrNull).term
				}
		}

// === map

fun <V, R> Term<V>.map(fn: (V) -> R): Term<R> =
	when (this) {
		is Term.Meta -> fn(value).metaTerm
		is Term.Structure -> Term.Structure(lhsTermOrNull?.map(fn), word, rhsTermOrNull?.map(fn))
	}
