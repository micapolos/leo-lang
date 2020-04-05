package leo.base

import java.net.URL

fun get(string: String): String =
	URL(string).readText()
