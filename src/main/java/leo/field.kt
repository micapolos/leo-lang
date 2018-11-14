package leo

import leo.base.Stream
import leo.base.appendableString
import leo.base.onlyStream
import leo.base.then

data class Field<out V>(
	val key: Word,
	val value: Term<V>) {
	override fun toString() = appendableString { it.append(this) }
}

fun <V> field(word: Word) =
	Field<V>(itWord, term(word))

infix fun <V> Word.fieldTo(value: Term<V>) =
	Field(this, value)

fun <V> Field<V>.termOrNull(key: Word): Term<V>? =
	if (this.key == key) value else null

// === appendable

fun <V> Appendable.appendCore(field: Field<V>): Appendable =
	this
		.append(field.key)
		.append('(')
		.appendCore(field.value)
		.append(')')

fun <V> Appendable.append(field: Field<V>): Appendable =
	this
		.append(field.key)
		.let { appendable ->
			if (field.value is Term.Structure && field.value.fieldStack.pop != null)
				appendable
					.append('(')
					.append(field.value)
					.append(')')
			else
				appendable
					.append(' ')
					.append(field.value)
		}

// === tokens

fun <V, R> Field<V>.foldTokens(folded: R, fn: (R, Token<V>) -> R): R =
	fn(value.foldTokens(fn(fn(folded, token(key)), beginToken()), fn), endToken())

// === reflect

fun <V> Field<V>.reflect(reflectValue: (V) -> Term<Value>): Field<Value> =
	fieldWord fieldTo term(
		keyWord fieldTo term(key.reflect),
		valueWord fieldTo term(value.reflect(reflectValue))
	)

fun <V> Field<V>?.orNullField(word: Word): Field<V> =
	this ?: word fieldTo term(nullWord)

fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
	key fieldTo value.map(fn)

fun <V> V?.orNullReflect(word: Word, reflect: V.() -> Field<Value>): Field<Value> =
	this?.let(reflect) ?: word.fieldTo(term(nullWord))

// === fold bytes

fun <V> Field<V>.byteStream(metaByteStream: (V) -> Stream<Byte>): Stream<Byte> =
	key.byteStream
		.then('('.toByte().onlyStream)
		.then(value.byteStream(metaByteStream))
		.then(')'.toByte().onlyStream)
