package leo16

import leo13.fold
import leo13.zipFoldOrNull
import leo15.*

fun Value.matches(value: Value): Boolean =
	value == value(anythingName()) || fieldsMatch(value)

fun Value.fieldsMatch(value: Value): Boolean =
	true
		.zipFoldOrNull(fieldStack, value.fieldStack) { lhs, rhs -> and(lhs.matches(rhs)) }
		?: false

fun Field.matches(field: Field): Boolean =
	when (field) {
		is SentenceField -> matches(field.sentence)
		is TakingField -> this is TakingField && taking.pattern == field.taking.pattern
		is DictionaryField -> this is DictionaryField && dictionary.matches(field.dictionary)
		is NativeField -> this is NativeField && native == field.native
		is ChoiceField -> matches(field.choice)
	}

fun Field.matches(choice: Choice): Boolean =
	this is ChoiceField && this.choice == choice || matchesCase(choice)

fun Field.matchesCase(choice: Choice): Boolean =
	false.fold(choice.caseFieldStack) { or(matches(it)) }

fun Field.matches(sentence: Sentence): Boolean =
	when (this) {
		is SentenceField -> this.sentence.matches(sentence)
		is TakingField -> sentence.word == takingName && taking.pattern == sentence.value.pattern
		is DictionaryField -> sentence == dictionaryName.sentenceTo()
		is NativeField -> sentence == nativeName.sentenceTo()
		is ChoiceField -> choice.matches(sentence)
	}

fun Sentence.matches(sentence: Sentence): Boolean =
	word == sentence.word && value.matches(sentence.value)

fun Function.matches(function: Function): Boolean =
	this == function

fun Dictionary.matches(dictionary: Dictionary): Boolean =
	this == dictionary

fun Choice.matches(sentence: Sentence): Boolean =
	sentence.word == choiceName &&
		true
			.zipFoldOrNull(caseFieldStack, sentence.value.fieldStack) { lhs, rhs -> and(lhs.matches(rhs)) }
		?: false