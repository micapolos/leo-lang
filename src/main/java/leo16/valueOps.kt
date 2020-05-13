package leo16

import leo.base.Seq
import leo.base.The
import leo.base.notNullIf
import leo.base.reverseStack
import leo.base.seq
import leo.base.then
import leo13.Stack
import leo13.mapFirst
import leo13.mapOrNull
import leo13.onlyOrNull
import leo16.names.*

inline fun <R> Value.normalize(field: Field, fn: Value.(Field) -> R): R {
	val wordOrNull = field.onlyWordOrNull
	return if (wordOrNull == null) fn(field)
	else value().fn(wordOrNull(this))
}

val Value.contentOrNull: Value?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.value

infix fun Value.getOrNull(word: String): Value? =
	contentOrNull?.accessOrNull(word)

infix fun Value.accessOrNull(word: String): Value? =
	fieldStack.mapFirst {
		accessOrNull(word)
	}

val Field.selectWord: String
	get() =
		when (this) {
			is SentenceField -> sentence.word
			is FunctionField -> _function
			is DictionaryField -> _dictionary
			is NativeField -> _native
			is ChoiceField -> _choice
			is LazyField -> _lazy
		}

infix fun Field.accessOrNull(word: String): Value? =
	notNullIf(word == selectWord) {
		value(this)
	}

infix fun Value.make(word: String): Value =
	value(word.invoke(this))

val Value.matchFieldOrNull: Field?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.value?.onlyFieldOrNull

val Value.theNativeOrNull: The<Any?>?
	get() =
		onlyFieldOrNull?.theNativeOrNull

val Value.loadedDictionaryOrNull: Dictionary?
	get() =
		dictionaryOrNull ?: loadedOrNull?.dictionaryOrNull

val Value.wordOrNullSeq: Seq<String?>
	get() =
		seq {
			onlyFieldOrNull?.sentenceOrNull?.run {
				word then value.wordOrNullSeq
			}
		}

val Value.wordStackOrNull: Stack<String>?
	get() =
		wordOrNullSeq.reverseStack.mapOrNull { this }

val Field.caseFieldOrNull: Field?
	get() =
		matchPrefix(_case) { rhs ->
			rhs.onlyFieldOrNull
		}

fun Value.rhsOrNull(word: String): Value? =
	onlyFieldOrNull?.rhsOrNull(word)

fun Field.rhsOrNull(word: String): Value? =
	matchPrefix(word) { it }

fun Value.pairOrNull(word: String): Pair<Value, Value>? =
	matchInfix(word) { lhs, rhs -> lhs to rhs }
