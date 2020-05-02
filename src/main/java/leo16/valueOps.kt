package leo16

import leo.base.notNullIf
import leo13.linkOrNull
import leo13.mapFirst
import leo13.onlyOrNull
import leo15.*

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

fun Value.listAppendOrNull(field: Field): Value? =
	matchPrefix(listName) { rhs ->
		value(listName(rhs.plus(field)))
	}

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