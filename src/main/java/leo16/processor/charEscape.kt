package leo16.processor

const val escapeChar = '\\'

val Char.escapedCharOrNull
	get() =
		when (this) {
			escapeChar -> escapeChar
			'n' -> '\n'
			't' -> '\t'
			'"' -> '"'
			else -> null
		}
