package leo

import leo.base.*

data class Field<out V>(
	val key: Word,
	val value: Term<V>) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun <V> Word.fieldTo(term: Term<V>) =
	Field(this, term)

val Word.itField: Field<Nothing>
	get() =
		itWord fieldTo term

val <V> Term<V>.itField: Field<V>
	get() =
		itWord fieldTo this

fun <V> Field<V>.get(key: Word): Term<V>? =
	if (this.key == key) value else null

// === appendable

fun <V> Appendable.append(field: Field<V>): Appendable =
	this
		.appendString(field.key)
		.run {
			when {
				field.value.isSimple -> append(' ').append(field.value)
				else -> append('(').append(field.value).append(')')
			}
		}

// === bit stream

val <V> Field<V>.tokenStream: Stream<Token<V>>
	get() =
		key.token.then {
			begin.control.token.then {
				value.tokenStream.then {
					end.control.token.onlyStream
				}
			}
		}

// === reflect

fun <V> Field<V>.reflectMeta(metaValueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	key fieldTo value.reflectMeta(metaValueReflect)

fun <V> V?.orNullReflect(word: Word, reflect: V.() -> Field<Nothing>): Field<Nothing> =
	this?.let(reflect) ?: word.fieldTo(nullWord.term())

fun <V> Field<V>.reflect(metaValueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	fieldWord fieldTo term(reflectMeta(metaValueReflect))

val Field<Nothing>.reflect: Field<Nothing>
	get() =
		reflect { fail }

// === parse

val Field<Nothing>.parseField: Field<Nothing>?
	get() =
		matchKey(fieldWord) {
			onlyFieldOrNull?.parseFieldMeta { fail }
		}

fun <V> Field<Nothing>.parseFieldMeta(parseMetaValue: (Field<Nothing>) -> V?): Field<V>? =
	value.parseTermMeta(parseMetaValue)?.let { parsedTerm ->
		key fieldTo parsedTerm
	}

// === map

fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
	key fieldTo value.map(fn)

// === match

fun <V, R : Any> Field<V>.matchKey(key: Word, fn: Term<V>.() -> R?): R? =
	if (this.key == key) fn(value)
	else null
