package leo.lab

import leo.*
import leo.base.*

data class Field<out V>(
	val value: V,
	val word: Word,
	val termOrNull: Term<V>?) {
	override fun toString() = appendableString { it.append(this) }
}

val Word.field: Field<Unit>
	get() =
		fieldTo(null)

infix fun Word.fieldTo(termOrNull: Term<Unit>?) =
	Field(Unit, this, termOrNull)

fun <V> Field<V>.get(key: Word): The<Term<V>?>? =
	if (this.word == key) termOrNull.the else null

// === appendable

fun <V> Appendable.append(field: Field<V>): Appendable =
	this
		.appendString(field.word)
		.let { appendable ->
			if (field.termOrNull == null) appendable
			else if (field.termOrNull.isSimple) append(' ').append(field.termOrNull)
			else append('(').append(field.termOrNull).append(')')
		}

// === byte stream

val <V> Field<V>.byteStream: Stream<Byte>
	get() =
		word.byteStream
			.then('('.toByte().onlyStream)
			.thenIfNotNull(termOrNull?.byteStream)
			.then(')'.toByte().onlyStream)

// === reflect

val <V> Field<V>.reflect: Field<Unit>
	get() =
		fieldWord fieldTo term(
			word.labReflect,
			termOrNull.orNullReflect(termWord, Term<V>::reflect))

fun <V> V?.orNullReflect(word: Word, reflect: V.() -> Field<Unit>): Field<Unit> =
	this?.let(reflect) ?: word.fieldTo(nullWord.term)

//fun <V> Field<V>?.orNullField(word: Word): Field<V> =
//	this ?: word fieldTo term(nullWord)

fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
	Field(fn(value), word, termOrNull?.map(fn))
