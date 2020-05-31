package leo16.lambda.typed

import leo.base.failIfOr
import leo.base.ifOrNull
import leo.base.notNullIf
import leo15.lambda.invoke
import leo16.lambda.type.Type

fun Typed.accessOrNull(word: String): Typed? =
	bodyTyped.linkTypedOrNull?.accessOrNull(word)

fun LinkTyped.accessOrNull(word: String): Typed? =
	lastSentenceTyped.accessOrNull(word) ?: previousTyped.accessOrNull(word)

fun SentenceTyped.accessOrNull(word: String): Typed? =
	notNullIf(word == sentence.word) { typed(this) }

fun Typed.getOrNull(word: String): Typed? =
	this.thingOrNull?.accessOrNull(word)

val Typed.thingOrNull: Typed?
	get() =
		bodyTyped.linkTypedOrNull?.onlySentenceTyped?.rhsTyped

fun Typed.matchOrNull(whenFirst: Typed, whenSecond: Typed): Typed? =
	alternativeTypedOrNull?.let { alternative ->
		whenFirst.functionTypedOrNull?.let { firstFunction ->
			whenSecond.functionTypedOrNull?.let { secondFunction ->
				ifOrNull(firstFunction.function.input == alternative.alternative.firstType) {
					ifOrNull(secondFunction.function.input == alternative.alternative.secondType) {
						ifOrNull(firstFunction.function.output == secondFunction.function.output) {
							alternative.term
								.invoke(firstFunction.term)
								.invoke(secondFunction.term) of firstFunction.function.output
						}
					}
				}
			}
		}
	}

fun Typed.assertType(type: Type): Typed =
	failIfOr(this.type != type) { this }

fun FunctionTyped.applyOrNull(typed: Typed): Typed? =
	notNullIf(typed.type == function.input) {
		term.invoke(typed.term) of function.output
	}