package leo16

import leo.base.The
import leo.base.notNullIf
import leo.base.the
import leo13.*
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

data class DictionaryField(val dictionary: Dictionary) : Field() {
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
val Dictionary.field: Field get() = DictionaryField(this)
val Any?.nativeField: Field get() = NativeField(this)
val Any?.nativeValue: Value get() = nativeField.value
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
val Field.dictionaryOrNull: Dictionary? get() = (this as? DictionaryField)?.dictionary
val Field.theNativeOrNull: The<Any?>? get() = if (this is NativeField) native.the else null
val Value.onlyFieldOrNull: Field? get() = fieldStack.onlyOrNull
val Value.sentenceOrNull: Sentence? get() = onlyFieldOrNull?.sentenceOrNull
val Value.functionOrNull: Function? get() = onlyFieldOrNull?.functionOrNull
val Value.dictionaryOrNull: Dictionary? get() = onlyFieldOrNull?.dictionaryOrNull
val Value.isEmpty: Boolean get() = fieldStack.isEmpty

val Sentence.onlyWordOrNull: String? get() = notNullIf(value.isEmpty) { word }
val Field.onlyWordOrNull: String? get() = sentenceOrNull?.onlyWordOrNull

operator fun Value.plus(field: Field): Value = fieldStack.push(field).value
operator fun Value.plus(value: Value): Value = fieldStack.pushAll(value.fieldStack).value
