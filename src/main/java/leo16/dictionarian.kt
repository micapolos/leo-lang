package leo16

import leo14.Literal
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

fun Dictionarian.plus(literal: Literal): Dictionarian =
	dictionary.dictionarian(pattern.plus(literal.asField.patternField))

fun Dictionarian.clearPlus(definition: Definition): Dictionarian =
	dictionary.plus(definition).emptyDictionarian

fun Dictionarian.plusIs(value: Value): Dictionarian =
	clearPlus(pattern.definitionTo(value.body))

fun Dictionarian.plusGives(function: Function): Dictionarian =
	clearPlus(pattern.definitionTo(function.body))
