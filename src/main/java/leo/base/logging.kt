package leo.base

fun <T> T.logged(tag: String): T {
	println("[$tag] $this")
	return this
}