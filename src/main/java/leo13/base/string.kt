package leo13.base

fun linesString(vararg strings: String): String =
	listOf(*strings).joinToString("\n")