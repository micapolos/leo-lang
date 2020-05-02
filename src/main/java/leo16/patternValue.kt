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
			FunctionPatternField -> givingName(anyName())
			LibraryPatternField -> libraryName(anyName())
			is LiteralPatternField -> literal.field
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
			exactName -> true
			else -> false
		}