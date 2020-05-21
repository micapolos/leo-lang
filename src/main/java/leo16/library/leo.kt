package leo16.library

import leo16.emptyDictionary
import leo16.emptyEvaluated
import leo16.native.leoDictionary
import leo16.scopeWithPublic

fun main() {
	leo
}

val leo = emptyDictionary.scopeWithPublic(leoDictionary).emptyEvaluated
