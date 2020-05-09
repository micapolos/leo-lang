package leo16

import leo.base.runIf
import leo13.map
import leo16.names.*

val Pattern.value: Value
	get() =
		when (this) {
			AnyPattern -> value(_any())
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
		word(pattern.value).runIf(word.isPatternKeyword) { _exact(this) }

val PatternTaking.valueField: Field
	get() =
		_taking(pattern.value)

val String.isPatternKeyword: Boolean
	get() =
		when (this) {
			_any -> true
			_exact -> true
			else -> false
		}