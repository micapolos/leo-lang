package leo16

import leo.base.runIf
import leo13.map
import leo15.anyName
import leo15.exactName
import leo15.takingName

val Pattern.value: Value
	get() =
		when (this) {
			AnyPattern -> value(anyName())
			is ValuePattern -> value.value
		}

val PatternValue.value: Value
	get() =
		fieldStack.map { valueField }.value

val PatternField.valueField: Field
	get() =
		when (this) {
			is SentencePatternField -> sentence.valueField
			is NativePatternField -> native.nativeField
			is TakingPatternField -> taking.valueField
		}

val PatternSentence.valueField: Field
	get() =
		word(pattern.value).runIf(word.isPatternKeyword) { exactName(this) }

val PatternTaking.valueField: Field
	get() =
		takingName(pattern.value)

val String.isPatternKeyword: Boolean
	get() =
		when (this) {
			anyName -> true
			exactName -> true
			else -> false
		}