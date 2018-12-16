package leo.term

import leo.Word

data class Field<out V>(
	val key: Word,
	val value: Term<V>)

infix fun <V> Word.fieldTo(term: Term<V>): Field<V> =
	Field(this, term)

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

fun <V : Any> Field<V>.get(key: Word): Term<V>? =
	if (key == this.key) value else null

val <V : Any> Field<V>.application: Application<V>
	get() =
		key apply value

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

//fun <V : Any, R : Any> Field<V>.map(fn: (V) -> R): Field<R> =
//	Field(key, value.mapfn(value))

//// === match
//
//fun <R : Any> Field.matchKey(key: Word, fn: Script.() -> R?): R? =
//	if (this.key == key) fn(value)
//	else null
