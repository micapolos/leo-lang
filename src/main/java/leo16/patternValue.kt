package leo16

import leo.base.runIf
import leo13.map
import leo15.anyName
import leo15.exactName
import leo15.givingName
import leo15.libraryName

val Pattern.value: Value
	get() =
		when (this) {
			AnyPattern -> value(anyName.invoke(value()))
			is ValuePattern -> value.value
		}

val PatternValue.value: Value
	get() =
		fieldStack.map { sentence.field }.value

val PatternField.sentence: Sentence
	get() =
		when (this) {
			is SentencePatternField -> sentence.sentence
			FunctionPatternField -> givingName(anyName(value()))
			LibraryPatternField -> libraryName(anyName.invoke(value()))
		}

val PatternSentence.sentence: Sentence
	get() =
		word.invoke(pattern.value).runIf(word.isPatternKeyword) { exactName(this) }

val String.isPatternKeyword: Boolean
	get() =
		when (this) {
			givingName -> true
			libraryName -> true
			anyName -> true
			exactName -> true
			else -> false
		}