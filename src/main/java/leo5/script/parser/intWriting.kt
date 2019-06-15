package leo5.script.parser

val String.digitIntOrNull
	get() = when (this) {
		"zero" -> 0
		"one" -> 1
		"two" -> 2
		"three" -> 3
		"four" -> 4
		"five" -> 5
		"six" -> 6
		"seven" -> 7
		"eight" -> 8
		"nine" -> 9
		else -> null
	}
