package leo15.type

import leo.base.applyOrNull
import leo.base.notNullIf

fun Typed.get(name: String): Typed? =
	emptyTyped.applyOrNull(rhsOrNull?.lineOrNull(name)) { plus(it) }

fun Typed.lineOrNull(name: String): TypedLine? =
	linkOrNull?.lineOrNull(name)

fun TypedLink.lineOrNull(name: String): TypedLine? =
	lhs.lineOrNull(name) ?: choice.onlyLineOrNull?.lineOrNull(name)

fun TypedLine.lineOrNull(name: String): TypedLine? =
	notNullIf(typeLine.name == name)
