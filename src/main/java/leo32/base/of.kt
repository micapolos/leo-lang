package leo32.base

data class Of<T>(
	val unit: Unit)

fun <T> of() = Of<T>(Unit)
