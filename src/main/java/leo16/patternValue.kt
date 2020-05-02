package leo16

import leo.base.runIf
import leo13.map
import leo15.*

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
			is LiteralPatternField -> literal.field
			is NativePatternField -> native.nativeField
		}

val PatternSentence.valueField: Field
	get() =
		word(pattern.value).runIf(word.isPatternKeyword) { exactName(this) }

val String.isPatternKeyword: Boolean
	get() =
		when (this) {
			givingName -> true
			libraryName -> true
			anyName -> true
			textName -> true
			nativeName -> true
			numberName -> true
			exactName -> true
			else -> false
		}