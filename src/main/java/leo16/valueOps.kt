package leo16

import leo.base.*
import leo13.Stack
import leo13.linkOrNull
import leo13.mapFirst
import leo13.mapOrNull
import leo13.onlyOrNull
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*

fun <R> Value.normalize(field: Field, fn: Value.(Field) -> R): R {
	val wordOrNull = field.onlyWordOrNull
	return if (wordOrNull == null) fn(field)
	else value().fn(wordOrNull(this))
}

val Value.thingOrNull: Value?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.value

val Value.isList: Boolean
	get() =
		matchPrefix(listName) { true } ?: false

infix fun Value.getOrNull(word: String): Value? =
	//ifOrNull(!isList) {
	thingOrNull?.accessOrNull(word)
//}

infix fun Value.accessOrNull(word: String): Value? =
	fieldStack.mapFirst {
		accessOrNull(word)
	}

val Field.selectWord: String
	get() =
		when (this) {
			is SentenceField -> sentence.word
			is TakingField -> takingName
			is DictionaryField -> dictionaryName
			is NativeField -> nativeName
			is ChoiceField -> choiceName
		}

val Literal.selectWord: String
	get() =
		when (this) {
			is StringLiteral -> textName
			is NumberLiteral -> numberName
		}

infix fun Field.accessOrNull(word: String): Value? =
	notNullIf(word == selectWord) {
		value(this)
	}

infix fun Value.make(word: String): Value =
	value(word.invoke(this))

val Value.matchValueOrNull: Value?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.let { sentence ->
			when (sentence.word) {
				listName -> sentence.value.listMatchValue
				else -> sentence.value
			}
		}

val Value.listMatchValue: Value
	get() =
		fieldStack.linkOrNull
			?.run { value(linkedName(previousName(listName(stack.value)), lastName(value))) }
			?: value(emptyName.invoke(value()))

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
		matchPrefix(caseName) { rhs ->
			rhs.onlyFieldOrNull
		}
