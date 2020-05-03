package leo16

import leo15.*

enum class Mode {
	EVALUATE,
	DEFINE,
	QUOTE
}

val String.wordIsMeta: Boolean
	get() =
		when (this) {
			defineName -> true
			givesName -> true
			givingName -> true
			dictionaryName -> true
			matchName -> true
			quoteName -> true
			else -> false
		}