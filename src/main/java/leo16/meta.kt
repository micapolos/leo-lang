package leo16

import leo15.*

val String.wordIsMeta: Boolean
	get() =
		when (this) {
			compileName -> true
			evaluateName -> true
			givesName -> true
			givingName -> true
			dictionaryName -> true
			matchName -> true
			quoteName -> true
			else -> false
		}