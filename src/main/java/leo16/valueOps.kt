package leo16

import leo.base.Seq
import leo.base.notNullIf
import leo.base.reverseStack
import leo.base.seq
import leo.base.then
import leo13.Stack
import leo13.mapOrNull
import leo14.unsignedBigDecimal
import leo16.names.*

inline fun <R> Value.normalize(sentence: Sentence, fn: Value.(Sentence) -> R): R {
	val wordOrNull = sentence.onlyWordOrNull
	return if (wordOrNull == null) fn(sentence)
	else value().fn(wordOrNull(this))
}

val Value.thingOrNull: Value?
	get() =
		onlySentenceOrNull?.rhsValue

infix fun Value.getOrNull(word: String): Value? =
	thingOrNull?.accessOrNull(word)

operator fun Value.get(word: String): Value =
	getOrNull(word)!!

infix fun Value.accessOrNull(word: String): Value? =
	when (this) {
		EmptyValue -> null
		is LinkValue -> link.accessOrNull(word)
		is NativeValue -> notNullIf(word == _native)
		is FunctionValue -> notNullIf(word == _function)
		is LazyValue -> notNullIf(word == _lazy)
		is FuncValue -> notNullIf(word == _function)
	}

infix fun ValueLink.accessOrNull(word: String): Value? =
	lastSentence.accessOrNull(word) ?: previousValue.accessOrNull(word)

infix fun Sentence.accessOrNull(word: String): Value? =
	notNullIf(word == this.word) {
		value(this)
	}

infix fun Value.make(word: String): Value =
	value(word.invoke(this))

val Value.matchWordOrNull: String?
	get() =
		when (this) {
			EmptyValue -> null
			is LinkValue -> onlySentenceOrNull?.word
			is NativeValue -> _native
			is FunctionValue -> _function
			is LazyValue -> _lazy
			is FuncValue -> _function
		}

val Value.loadedDictionaryOrNull: Dictionary?
	get() =
		loadedOrNull?.scope?.exportDictionary

val Value.wordOrNullSeq: Seq<String?>
	get() =
		seq {
			onlySentenceOrNull?.run {
				word then rhsValue.wordOrNullSeq
			}
		}

val Value.wordStackOrNull: Stack<String>?
	get() =
		wordOrNullSeq.reverseStack.mapOrNull { this }

fun Value.rhsOrNull(word: String): Value? =
	onlySentenceOrNull?.rhsOrNull(word)

fun Sentence.rhsOrNull(word: String): Value? =
	matchPrefix(word) { it }

fun Value.pairOrNull(word: String): Pair<Value, Value>? =
	matchInfix(word) { lhs, rhs -> lhs to rhs }

val Value.hashBigDecimal
	get() =
		hashCode().unsignedBigDecimal

fun <T : Any> T?.orNullAsSentence(word: String, fn: T.() -> Sentence): Sentence =
	if (this == null) word(_none())
	else fn()

fun Value.matching(patternValue: Value): Value =
	if (patternValue.matches(this)) this
	else throw AssertionError(value(_error(this.plus(_matching(patternValue)))))

val Value.force: Value
	get() =
		forceOrNull ?: this

val Value.forceOrNull: Value?
	get() =
		lazyOrNull?.evaluate

val ValueLink.normalize
	get() =
		lastSentence.matchWord { word ->
			previousValue.make(word)
		}

val Value.normalize
	get() =
		linkOrNull?.normalize ?: this