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

data class LiteralField(val literal: Literal) : Field() {
	override fun toString() = super.toString()
}

data class NativeField(val native: Any?) : Field() {
	override fun toString() = super.toString()
}

data class Sentence(val word: String, val value: Value) {
	override fun toString() = scriptLine.string
}

val Value.asField: Field
	get() =
		valueName(this)

val Stack<Field>.value: Value get() = Value(this)
val Sentence.field: Field get() = SentenceField(this)
val Function.field: Field get() = FunctionField(this)
val Library.field: Field get() = LibraryField(this)
val Literal.field: Field get() = LiteralField(this)
val Any?.nativeField: Field get() = NativeField(this)
fun value(vararg fields: Field) = stack(*fields).value
fun value(sentence: Sentence, vararg sentences: Sentence) = stack(sentence, *sentences).map { field }.value
infix fun String.sentenceTo(value: Value) = Sentence(this, value)
fun String.sentenceTo(vararg fields: Field): Sentence = sentenceTo(stack(*fields).value)
operator fun String.invoke(value: Value): Field = Sentence(this, value).field
operator fun String.invoke(vararg fields: Field): Field = invoke(stack(*fields).value)
operator fun String.invoke(sentence: Sentence, vararg sentences: Sentence): Field = invoke(value(sentence, *sentences))
val Field.value get() = value(this)

val Field.sentenceOrNull: Sentence? get() = (this as? SentenceField)?.sentence
val Field.functionOrNull: Function? get() = (this as? FunctionField)?.function
val Field.libraryOrNull: Library? get() = (this as? LibraryField)?.library
val Field.literalOrNull: Literal? get() = (this as? LiteralField)?.literal
val Field.nativeOrNull: Any? get() = (this as? NativeField)?.native
val Value.onlyFieldOrNull: Field? get() = fieldStack.onlyOrNull
val Value.sentenceOrNull: Sentence? get() = onlyFieldOrNull?.sentenceOrNull
val Value.functionOrNull: Function? get() = onlyFieldOrNull?.functionOrNull
val Value.libraryOrNull: Library? get() = onlyFieldOrNull?.libraryOrNull
val Value.isEmpty: Boolean get() = fieldStack.isEmpty

val Sentence.onlyWordOrNull: String? get() = notNullIf(value.isEmpty) { word }
val Field.onlyWordOrNull: String? get() = sentenceOrNull?.onlyWordOrNull

operator fun Value.plus(field: Field): Value = fieldStack.push(field).value
operator fun Value.plus(value: Value): Value = fieldStack.pushAll(value.fieldStack).value
