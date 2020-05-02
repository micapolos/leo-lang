package leo16

import leo.base.notNullIf
import leo13.*
import leo15.valueName

data class Value(val fieldStack: Stack<Field>) {
	override fun toString() = super.toString()
}

sealed class Field {
	override fun toString() = asSentence.toString()
}

data class SentenceField(val sentence: ValueSentence) : Field() {
	override fun toString() = super.toString()
}

data class FunctionField(val function: Function) : Field() {
	override fun toString() = super.toString()
}

data class LibraryField(val library: Library) : Field() {
	override fun toString() = super.toString()
}

//data class StringField(val string: String): Field() {
//	override fun toString() = super.toString()
//}

data class ValueSentence(val word: String, val value: Value) {
	override fun toString() = asSentence.toString()
}

val Value.asSentence: Sentence
	get() =
		valueName(asScript)

val Value.asScript: Script
	get() =
		fieldStack.map { asSentence }.script

val Field.asSentence: Sentence
	get() =
		when (this) {
			is SentenceField -> sentence.asSentence
			is FunctionField -> function.asSentence
			is LibraryField -> library.asSentence
		}

val ValueSentence.asSentence: Sentence
	get() =
		word.invoke(value.asScript)

val Stack<Field>.value: Value get() = Value(this)
val ValueSentence.field: Field get() = SentenceField(this)
val Function.field: Field get() = FunctionField(this)
val Library.field: Field get() = LibraryField(this)
fun value(vararg fields: Field) = stack(*fields).value
operator fun String.invoke(value: Value) = ValueSentence(this, value).field
operator fun String.invoke(field: Field, vararg fields: Field) = invoke(stack(field, *fields).value)
val Field.value get() = value(this)

val Field.sentenceOrNull: ValueSentence? get() = (this as? SentenceField)?.sentence
val Field.functionOrNull: Function? get() = (this as? FunctionField)?.function
val Field.libraryOrNull: Library? get() = (this as? LibraryField)?.library
val Value.onlyFieldOrNull: Field? get() = fieldStack.onlyOrNull
val Value.sentenceOrNull: ValueSentence? get() = onlyFieldOrNull?.sentenceOrNull
val Value.functionOrNull: Function? get() = onlyFieldOrNull?.functionOrNull
val Value.libraryOrNull: Library? get() = onlyFieldOrNull?.libraryOrNull
val Value.isEmpty: Boolean get() = fieldStack.isEmpty

val ValueSentence.onlyWordOrNull: String? get() = notNullIf(value.isEmpty) { word }
val Field.onlyWordOrNull: String? get() = sentenceOrNull?.onlyWordOrNull

operator fun Value.plus(field: Field): Value = fieldStack.push(field).value
operator fun Value.plus(value: Value): Value = fieldStack.pushAll(value.fieldStack).value
