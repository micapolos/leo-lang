package leo16

import leo15.*

val String.wordIsMeta: Boolean
	get() =
		when (this) {
			compileName -> true
			evaluateName -> true
			givesName -> true
			givingName -> true
			matchName -> true
			scriptName -> true
			importName -> true
			else -> false
		}