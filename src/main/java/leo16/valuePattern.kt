package leo16

import leo13.fold
import leo13.map
import leo13.reverse
import leo16.names.*

val Value.pattern: Pattern
	get() =
		emptyPattern.fold(fieldStack.reverse) { plus(it) }

fun Pattern.plus(field: Field): Pattern =
	if (field == _any() && isEmpty) anyPattern
	else plus(field.patternField)

val Field.patternField: PatternField
	get() =
		when (this) {
			is SentenceField -> sentence.patternField
			is GivesField -> function.asPatternField.patternField
			is DictionaryField -> dictionary.printSentence.patternField
			is NativeField -> nativePatternField
			is ChoiceField -> choice.patternField
		}

val Sentence.patternField: PatternField
	get() =
		when (word) {
			_taking -> value.pattern.taking.field
			else -> exactPatternField
		}

val Choice.patternField: PatternField
	get() =
		_choice.invoke(caseFieldStack.map { patternField }.value.pattern)

val Sentence.exactPatternField: PatternField
	get() =
		word.invoke(value.pattern)
