package leo14.typed

import leo14.Literal
import leo14.Script
import leo14.literal

val Any.anyLiteralOrNull: Literal?
	get() = when (this) {
		is String -> literal(this)
		is Int -> literal(this)
		is Double -> literal(this)
		else -> null
	}

val Typed<Any>.anyDecompile: Script
	get() =
		decompile(Any::anyLiteralOrNull)