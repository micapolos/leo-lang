package leo15

import leo.base.notNullIf
import leo14.NumberLiteral
import leo14.StringLiteral

fun Typed.get(name: String): Typed? =
	linkOrNull?.onlyLineOrNull?.fieldOrNull?.rhs?.lineOrNull(name)?.let { typed(it) }

fun Typed.lineOrNull(name: String): TypedLine? =
	linkOrNull?.lineOrNull(name)

fun TypedLink.lineOrNull(name: String): TypedLine? =
	lhs.lineOrNull(name) ?: line.lineOrNull(name)

// TODO: handle meta!!!
fun TypedLine.lineOrNull(name: String): TypedLine? =
	when (typeLine) {
		is LiteralTypeLine ->
			when (typeLine.literal) {
				is StringLiteral -> notNullIf(name == textName)
				is NumberLiteral -> notNullIf(name == numberName)
			}
		is FieldTypeLine ->
			notNullIf(typeLine.field.name == name)
		JavaTypeLine ->
			notNullIf(name == javaName)
	}

