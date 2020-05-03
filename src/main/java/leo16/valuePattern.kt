package leo16

import leo.base.notNullIf
import leo13.map
import leo15.anyName

val Value.pattern: Pattern
	get() =
		null
			?: anyPatternOrNull
			?: valuePattern

val Value.anyPatternOrNull: Pattern?
	get() =
		notNullIf(this == value(anyName(value()))) {
			anyPattern
		}

val Value.valuePattern: Pattern
	get() =
		fieldStack.map { patternField }.value.pattern

val Field.patternField: PatternField
	get() =
		when (this) {
			is SentenceField -> sentence.patternField
			is FunctionField -> function.printSentence.patternField
			is DictionaryField -> dictionary.printSentence.patternField
			is NativeField -> nativePatternField
		}

val Sentence.patternField: PatternField
	get() =
		// TODO: pattern keywords
		exactPatternField

val Sentence.exactPatternField: PatternField
	get() =
		word.invoke(value.pattern)
