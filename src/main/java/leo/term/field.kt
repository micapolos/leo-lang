package leo.term

import leo.Word

data class Field<out V>(
	val key: Key,
	val value: Value<V>) {
	//override fun toString() = appendableString { it.append(this) }
}

infix fun <V> Key.fieldTo(value: Value<V>): Field<V> =
	Field(this, value)

infix fun <V> Word.fieldTo(term: Term<V>): Field<V> =
	key.fieldTo(term.value)

//infix fun <V> Word.field2To(word: Word): Field2<V> =
//	field2To(word.term2())
//
//infix fun <V> Word.fieldTo(value: V): Field<V> =
//	fieldTo(value.meta.term)
//
//val Word.itField: Field<Nothing>
//	get() =
//		itWord fieldTo term
//
//val <V> Term<V>.itField: Field<V>
//	get() =
//		itWord fieldTo this

fun <V> Field<V>.get(key: Key): Value<V>? =
	if (this.key == key) value else null

// === appendable

//fun <V> Appendable.append(field: Field<V>): Appendable =
//	this
//		.appendString(field.key)
//		.run {
//			when {
//				field.value.isSimple -> append(' ').append(field.value)
//				else -> append('(').append(field.value).append(')')
//			}
//		}
//
////// === bit stream
//
//val <V> Field<V>.tokenStream: Stream<Token<V>>
//	get() =
//		key.token.then {
//			begin.control.token.then {
//				value.tokenStream.then {
//					end.control.token.onlyStream
//				}
//			}
//		}
//
//// === reflect
//
//val Field<Nothing>.reflect: Field<Nothing>
//	get() =
//		fieldWord fieldTo this.term
//
//fun <V> Field<V>.reflectMetaTerm(valueReflect: V.() -> Term<Nothing>): Field<Nothing> =
//	key fieldTo value.reflectMetaTerm(valueReflect)
//
//fun <V> V?.orNullReflect(word: Word, reflect: V.() -> Field<Nothing>): Field<Nothing> =
//	this?.let(reflect) ?: word.fieldTo(nullWord.term())
//
//// === parse
//
//val Field<Nothing>.parseField: Field<Nothing>?
//	get() =
//		matchKey(fieldWord) {
//			onlyFieldOrNull?.parseField { fail }
//		}
//
//fun <V> Field<Nothing>.parseField(parseMetaValue: (Term<Nothing>) -> V?): Field<V>? =
//	value.parseTerm(parseMetaValue)?.let { parsedTerm ->
//		key fieldTo parsedTerm
//	}
//
// === map

fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
	Field(key, value.map(fn))
//
//// === match
//
//fun <R : Any> Field.matchKey(key: Word, fn: Script.() -> R?): R? =
//	if (this.key == key) fn(value)
//	else null
