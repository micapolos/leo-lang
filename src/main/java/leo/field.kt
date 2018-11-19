package leo

import leo.base.*

data class Field<out V>(
	val word: Word,
	val termOrNull: Term<V>?) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun <V> Word.fieldTo(termOrNull: Term<V>?) =
	Field(this, termOrNull)

fun <V> Word.field(): Field<V> =
	fieldTo(null)

val Word.field: Field<Nothing>
	get() =
		field()

fun <V> Field<V>.get(key: Word): The<Term<V>?>? =
	if (this.word == key) termOrNull.the else null

// === appendable

fun <V> Appendable.append(field: Field<V>): Appendable =
	this
		.appendString(field.word)
		.let { appendable ->
			when {
				field.termOrNull == null -> appendable
				field.termOrNull.isSimple -> append(' ').append(field.termOrNull)
				else -> append('(').append(field.termOrNull).append(')')
			}
		}

// === bit stream

val Field<Nothing>.tokenStream: Stream<Token<Nothing>>
	get() =
		word.beginToken.onlyStream
			.then { termOrNull?.tokenStream }
			.then { endToken.onlyStream }

val <V> Field<V>.reversedTokenStream: Stream<Token<V>>
	get() =
		endToken<V>().onlyStream
			.then { termOrNull?.reversedTokenStream }
			.then { word.beginToken<V>().onlyStream }

// === reflect

fun <V> Field<V>.reflect(metaReflect: V.() -> Field<Nothing>): Field<Nothing> =
	fieldWord fieldTo term(
		word.reflect,
		termOrNull.orNullReflect(termWord) { reflect(metaReflect) })

fun <V> V?.orNullReflect(word: Word, reflect: V.() -> Field<Nothing>): Field<Nothing> =
	this?.let(reflect) ?: word.fieldTo(nullWord.term())

//fun <V> Field<V>?.orNullField(word: Word): Field<V> =
//	this ?: word fieldTo term(nullWord)

// === map

fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
	word fieldTo termOrNull?.map(fn)

// === match

fun <V, R> Field<V>.match(key: Word, fn: (Term<V>?) -> R): R? =
	if (word == key) fn(termOrNull)
	else null
