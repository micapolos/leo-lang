package leo16

import leo.base.notNullIf
import leo13.*
import leo14.Literal
import leo15.string

data class Value(val fieldStack: Stack<Field>) {
	override fun toString() = script.string
}

sealed class Field {
	override fun toString() = scriptLine.string
}

data class SentenceField(val sentence: Sentence) : Field() {
	override fun toString() = super.toString()
}

data class FunctionField(val function: Function) : Field() {
	override fun toString() = super.toString()
}

data class LibraryField(val library: Library) : Field() {
	override fun toString() = super.toString()
}

//data class LiteralField(val literal: Literal): Field() {
//	override fun toString() = super.toString()
//}

data class Sentence(val word: String, val value: Value) {
	override fun toString() = scriptLine.string
}

val Value.asSentence: Sentence
	get() =
		valueName(this)

val Field.asSentence: Sentence
	get() =
		when (this) {
			is SentenceField -> sentence
			is FunctionField -> function.asSentence
			is LibraryField -> library.asSentence
		}

val Stack<Field>.value: Value get() = Value(this)
val Sentence.field: Field get() = SentenceField(this)
val Function.field: Field get() = FunctionField(this)
val Library.field: Field get() = LibraryField(this)
fun value(vararg fields: Field) = stack(*fields).value
fun value(sentence: Sentence, vararg sentences: Sentence) = stack(sentence, *sentences).map { field }.value
operator fun String.invoke(value: Value) = Sentence(this, value)
operator fun String.invoke(vararg fields: Field) = invoke(stack(*fields).value)
operator fun String.invoke(sentence: Sentence, vararg sentences: Sentence) = invoke(value(sentence, *sentences))
val Field.value get() = value(this)

val Field.sentenceOrNull: Sentence? get() = (this as? SentenceField)?.sentence
val Field.functionOrNull: Function? get() = (this as? FunctionField)?.function
val Field.libraryOrNull: Library? get() = (this as? LibraryField)?.library
val Value.onlyFieldOrNull: Field? get() = fieldStack.onlyOrNull
val Value.sentenceOrNull: Sentence? get() = onlyFieldOrNull?.sentenceOrNull
val Value.functionOrNull: Function? get() = onlyFieldOrNull?.functionOrNull
val Value.libraryOrNull: Library? get() = onlyFieldOrNull?.libraryOrNull
val Value.isEmpty: Boolean get() = fieldStack.isEmpty

val Sentence.onlyWordOrNull: String? get() = notNullIf(value.isEmpty) { word }
val Field.onlyWordOrNull: String? get() = sentenceOrNull?.onlyWordOrNull

operator fun Value.plus(field: Field): Value = fieldStack.push(field).value
operator fun Value.plus(value: Value): Value = fieldStack.pushAll(value.fieldStack).value
