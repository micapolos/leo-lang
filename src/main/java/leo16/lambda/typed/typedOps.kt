package leo16.lambda.typed

import leo.base.notNullIf
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
