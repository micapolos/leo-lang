package leo16

import leo15.givesName
import leo15.isName

data class Dictionarian(val dictionary: Dictionary, val pattern: Pattern)

fun Dictionary.dictionarian(pattern: Pattern) = Dictionarian(this, pattern)
val Dictionary.emptyDictionarian get() = dictionarian(emptyPattern)

fun Dictionarian.begin(word: String): Reader =
	when (word) {
		isName -> dictionary.emptyScope.emptyEvaluator.reader
		givesName -> dictionary.emptyCompiler.reader
		else -> dictionary.emptyDictionarian.reader
	}
