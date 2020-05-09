package leo16

import leo.base.notNullIf
import leo13.map
import leo16.names.*

val Value.pattern: Pattern
	get() =
		null
			?: anyPatternOrNull
			?: valuePattern

val Value.anyPatternOrNull: Pattern?
	get() =
		notNullIf(this == value(_any(value()))) {
			anyPattern
		}

val Value.valuePattern: Pattern
	get() =
		fieldStack.map { patternField }.value.pattern

val Field.patternField: PatternField
	get() =
		when (this) {
			is SentenceField -> sentence.patternField
			is TakingField -> taking.asField.patternField
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
