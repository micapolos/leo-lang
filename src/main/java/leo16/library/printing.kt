package leo16.library

import leo16.emptyDictionary
import leo16.emptyEvaluated
import leo16.native.printingDictionary
import leo16.scopeWithPublic

fun main() {
	printing
}

val printing = emptyDictionary.scopeWithPublic(printingDictionary).emptyEvaluated
