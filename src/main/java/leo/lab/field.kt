package leo.lab

import leo.Word
import leo.base.appendString
import leo.base.appendableString

data class Field<out V>(
	val key: Word,
	val value: Script<V>) {
	override fun toString() = appendableString { it.append(this) }
}

fun <V> field(word: Word, script: Script<V>) =
	Field(word, script)

infix fun <V> Word.fieldTo(term: Script<V>) =
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

fun <V> Field<V>.get(key: Word): Script<V>? =
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

//// === bit stream
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
//// === map
//
//fun <V, R> Field<V>.map(fn: (V) -> R): Field<R> =
//	key fieldTo value.map(fn)
//
// === match

fun <V, R : Any> Field<V>.matchKey(key: Word, fn: Script<V>.() -> R?): R? =
	if (this.key == key) fn(value)
	else null
