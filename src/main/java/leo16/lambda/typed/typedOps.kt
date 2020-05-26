package leo16.lambda.typed

import leo.base.failIfOr
import leo.base.ifOrNull
import leo.base.notNullIf
import leo15.lambda.invoke
import leo16.lambda.type.Type
import leo16.lambda.type.selectWord

fun Typed.accessOrNull(word: String): Typed? =
	bodyTyped.linkTypedOrNull?.accessOrNull(word)

fun LinkTyped.accessOrNull(word: String): Typed? =
	lastFieldTyped.accessOrNull(word) ?: previousTyped.accessOrNull(word)

fun FieldTyped.accessOrNull(word: String): Typed? =
	notNullIf(word == field.selectWord) { typed }

fun Typed.getOrNull(word: String): Typed? =
	this.contentOrNull?.accessOrNull(word)

val Typed.contentOrNull: Typed?
	get() =
		bodyTyped.linkTypedOrNull?.onlyFieldTyped?.sentenceOrNull?.rhsTyped

fun Typed.matchOrNull(whenFirst: Typed, whenSecond: Typed): Typed? =
	alternativeTypedOrNull?.let { alternative ->
		whenFirst.typeFunctionOrNull?.let { firstFunction ->
			whenSecond.typeFunctionOrNull?.let { secondFunction ->
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