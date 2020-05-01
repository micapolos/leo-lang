package leo16

import leo15.*

val String.wordIsMeta: Boolean
	get() =
		when (this) {
			compileName -> true
			evaluateName -> true
			givesName -> true
			givingName -> true
			libraryName -> true
			matchName -> true
			scriptName -> true
			else -> false
		}