package leo16

import leo.base.The
import leo.base.notNullIf
import leo.base.the
import leo13.Link
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo13.linkTo
import leo13.map
import leo13.onlyOrNull
import leo13.push
import leo13.pushAll
import leo13.stack
import leo15.string
import leo16.names.*

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

data class NativeField(val native: Any?) : Field() {
	override fun toString() = super.toString()
}

data class ChoiceField(val choice: Choice) : Field() {
	override fun toString() = super.toString()
}

data class LazyField(val lazy: Lazy) : Field() {
	override fun toString() = super.toString()
}

data class EvaluatedField(val evaluated: Evaluated) : Field() {
	override fun toString() = super.toString()
}

data class Sentence(val word: String, val value: Value) {
	override fun toString() = scriptLine.string
}

data class Choice(val eitherStack: Stack<Either>) {
	override fun toString() = scriptLine.string
}

data class Either(val field: Field) {
	override fun toString() = scriptLine.string
}

val Value.asField: Field
	get() =
		_value(this)

val Stack<Field>.value: Value get() = Value(this)
val Sentence.field: Field get() = SentenceField(this)
val Function.field: Field get() = FunctionField(this)
val Choice.field: Field get() = ChoiceField(this)
val Lazy.field: Field get() = LazyField(this)
val Any?.nativeField: Field get() = NativeField(this)
val Any?.nativeValue: Value get() = nativeField.value
fun value(vararg fields: Field) = stack(*fields).value
fun value(sentence: Sentence, vararg sentences: Sentence) = stack(sentence, *sentences).map { field }.value
infix fun String.sentenceTo(value: Value) = Sentence(this, value)
fun String.sentenceTo(vararg fields: Field): Sentence = sentenceTo(stack(*fields).value)
operator fun String.invoke(value: Value): Field = Sentence(this, value).field
operator fun String.invoke(vararg fields: Field): Field = invoke(stack(*fields).value)
operator fun String.invoke(sentence: Sentence, vararg sentences: Sentence): Field = invoke(value(sentence, *sentences))
val Stack<Either>.choice: Choice get() = Choice(this)
val Field.value get() = value(this)
val emptyValue = value()

val Field.sentenceOrNull: Sentence? get() = (this as? SentenceField)?.sentence
val Field.functionOrNull: Function? get() = (this as? FunctionField)?.function
val Field.choiceOrNull: Choice? get() = (this as? ChoiceField)?.choice
val Field.lazyOrNull: Lazy? get() = (this as? LazyField)?.lazy
val Field.theNativeOrNull: The<Any?>? get() = if (this is NativeField) native.the else null
val Field.either get() = Either(this)
val Value.onlyFieldOrNull: Field? get() = fieldStack.onlyOrNull
val Value.sentenceOrNull: Sentence? get() = onlyFieldOrNull?.sentenceOrNull
val Value.functionOrNull: Function? get() = onlyFieldOrNull?.functionOrNull
val Value.isEmpty: Boolean get() = fieldStack.isEmpty

val Sentence.onlyWordOrNull: String? get() = notNullIf(value.isEmpty) { word }
val Field.onlyWordOrNull: String? get() = sentenceOrNull?.onlyWordOrNull

operator fun Value.plus(field: Field): Value = fieldStack.push(field).value
operator fun Value.plus(value: Value): Value = fieldStack.pushAll(value.fieldStack).value

val Value.linkOrNull: Link<Value, Field>?
	get() =
		fieldStack.linkOrNull?.run {
			stack.value linkTo value
		}

fun Value.does(compiled: Compiled): Field = pattern.does(compiled).field