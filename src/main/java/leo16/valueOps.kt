package leo16

import leo.base.*
import leo13.*
import leo13.Stack
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo15.*
import leo15.caseName
import leo15.choiceName
import leo15.emptyName
import leo15.itemName
import leo15.listName
import leo15.previousName
import leo15.textName
import leo16.names.*

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

val Value.matchFieldOrNull: Field?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.let { sentence ->
			when (sentence.word) {
				listName -> sentence.value.listMatchField
				else -> sentence.value.onlyFieldOrNull
			}
		}

val Value.listMatchField: Field?
	get() =
		if (this == value(_empty())) _empty()
		else fieldStack.linkOrNull?.let { fieldLink ->
			fieldLink.value.matchPrefix(_item) {
				_linked(
					_previous(_list(if (fieldLink.stack.isEmpty) value(emptyName()) else fieldLink.stack.value)),
					_last(fieldLink.value))
			}
		}

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
