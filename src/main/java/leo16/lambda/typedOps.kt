package leo16.lambda

import leo.base.notNullIf

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
