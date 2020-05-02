package leo16

import leo.base.notNullIf
import leo13.linkOrNull
import leo13.mapFirst
import leo13.onlyOrNull
import leo15.*

fun <R> Value.normalize(field: Field, fn: Value.(Field) -> R): R {
	val wordOrNull = field.onlyWordOrNull
	return if (wordOrNull == null) fn(field)
	else value().fn(wordOrNull(this).field)
}

val Value.thingOrNull: Value?
	get() =
		fieldStack.onlyOrNull?.sentenceOrNull?.value

infix fun Value.getOrNull(word: String): Value? =
	thingOrNull?.accessOrNull(word)

infix fun Value.accessOrNull(word: String): Value? =
	fieldStack.mapFirst {
		accessOrNull(word)
	}

val Field.selectWord: String
	get() =
		when (this) {
			is SentenceField -> sentence.word
			is FunctionField -> givingName
			is LibraryField -> libraryName
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
			?.run { value(linkName(previousName(listName(stack.value)), lastName(value))) }
			?: value(emptyName.invoke(value()))