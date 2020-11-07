package leo21.token.compiler

import leo21.dictionary.Dictionary
import leo21.token.processor.TokenProcessor

sealed class DefineParent

fun DefineParent.plus(dictionary: Dictionary): TokenProcessor =
	TODO()