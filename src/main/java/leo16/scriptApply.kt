package leo16

import leo13.onlyOrNull
import leo15.appendName
import leo15.lastName
import leo15.previousName

val Script.apply: Script?
	get() =
		null
			?: applyLast
			?: applyPrevious
			?: applyAppend
			?: applyGet

val Script.applyGet: Script?
	get() =
		matchLink { lhs, word, rhs ->
			lhs.matchEmpty {
				rhs.getOrNull(word)
			}
		}

val Script.applyLast: Script?
	get() =
		matchPrefix(lastName) { rhs ->
			rhs.lastOrNull
		}

val Script.applyPrevious: Script?
	get() =
		matchPrefix(previousName) { rhs ->
			rhs.previouOrNull
		}

val Script.applyAppend: Script?
	get() =
		matchInfix(appendName) { lhs, rhs ->
			rhs.sentenceStack.onlyOrNull?.let { sentence ->
				lhs.append(sentence)
			}
		}
