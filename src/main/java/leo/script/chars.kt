package leo.script

val Char.scriptInvert
	get() =
		when (this) {
			' ' -> '\u0000'
			'\u0000' -> ' '
			else -> this
		}
