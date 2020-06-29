package leo16

import leo.base.The
import leo.base.fold
import leo.base.notNullIf
import leo.base.runWith
import leo.base.the
import leo13.Stack
import leo13.array
import leo13.push
import leo13.stack
import leo14.untyped.dottedColorsParameter
import leo14.untyped.leoString
import leo16.names.*

sealed class Value {
	override fun toString() = dottedColorsParameter.runWith(false) { script.leoString }
}

object EmptyValue : Value()

data class LinkValue(val link: ValueLink) : Value() {
	override fun toString() = super.toString()
}

data class NativeValue(val native: Any?) : Value() {
	override fun toString() = super.toString()
}

data class FunctionValue(val function: Function) : Value() {
	override fun toString() = super.toString()
}

data class FuncValue(val func: Func) : Value() {
	override fun toString() = super.toString()
}

data class LazyValue(val lazy: Lazy) : Value() {
	override fun toString() = super.toString()
}

data class ValueLink(val previousValue: Value, val lastSentence: Sentence) {
	override fun toString() = dottedColorsParameter.runWith(false) { script.leoString }
}

data class Sentence(val word: String, val rhsValue: Value) {
	override fun toString() = dottedColorsParameter.runWith(false) { scriptLine.leoString }
}

val emptyValue: Value get() = EmptyValue
val ValueLink.value: Value get() = LinkValue(this)
val Function.value: Value get() = FunctionValue(this)
val Lazy.value: Value get() = LazyValue(this)
val Any?.nativeValue: Value get() = NativeValue(this)
fun Value.linkTo(sentence: Sentence) = ValueLink(this, sentence)
fun Value.plus(sentence: Sentence): Value = linkTo(sentence).value
val Sentence.onlyValue: Value get() = emptyValue.linkTo(this).value
fun value(vararg sentences: Sentence): Value = emptyValue.fold(sentences) { plus(it) }
infix fun String.sentenceTo(value: Value) = Sentence(this, value)
fun String.sentenceTo(vararg sentences: Sentence): Sentence = sentenceTo(value(*sentences))
operator fun String.invoke(value: Value): Sentence = sentenceTo(value)
operator fun String.invoke(vararg sentences: Sentence): Sentence = sentenceTo(value(*sentences))
//operator fun String.invoke(sentence: Sentence, vararg sentences: Sentence): Sentence = invoke(value(sentence, *sentences))

val Value.isEmpty get() = this is EmptyValue
val Value.functionOrNull: Function? get() = (this as? FunctionValue)?.function
val Value.lazyOrNull: Lazy? get() = (this as? LazyValue)?.lazy
val Value.theNativeOrNull: The<Any?>? get() = if (this is NativeValue) native.the else null
val Value.linkOrNull: ValueLink? get() = (this as? LinkValue)?.link
val Value.onlySentenceOrNull: Sentence? get() = linkOrNull?.onlySentenceOrNull
val ValueLink.onlySentenceOrNull: Sentence? get() = notNullIf(previousValue.isEmpty) { lastSentence }

val Sentence.onlyWordOrNull: String? get() = notNullIf(rhsValue.isEmpty) { word }

fun Value.does(compiled: Compiled): Value = functionTo(compiled).value

val Value.asSentence get() = _value(this)

val Value.sentenceStackOrNull: Stack<Sentence>?
	get() =
		when (this) {
			EmptyValue -> stack()
			is LinkValue -> link.previousValue.sentenceStackOrNull?.push(link.lastSentence)
			is NativeValue -> null
			is FunctionValue -> null
			is LazyValue -> null
			is FuncValue -> null
		}

val Stack<Sentence>.value: Value
	get() =
		value(*array)