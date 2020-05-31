package leo16.lambda.compiler

import leo13.Stack
import leo13.mapFirst
import leo16.lambda.typed.FunctionTyped
import leo16.lambda.typed.SentenceTyped
import leo16.lambda.typed.Typed
import leo16.lambda.typed.applyOrNull
import leo16.lambda.typed.plus

data class Dictionary(val functionStack: Stack<FunctionTyped>)
data class Compiled(val dictionary: Dictionary, val typed: Typed)

fun Dictionary.applyOrNull(typed: Typed): Typed? =
	functionStack.mapFirst { applyOrNull(typed) }

fun Dictionary.apply(typed: Typed): Typed =
	null
		?: applyOrNull(typed)
		?: typed.applyOrNull
		?: typed

fun Compiled.plus(field: SentenceTyped): Compiled =
	Compiled(dictionary, dictionary.apply(typed.plus(field)))
