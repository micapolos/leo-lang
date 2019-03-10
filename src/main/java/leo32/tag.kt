package leo32

data class Tag(
	val string: String)

val String.tag
	get() =
		Tag(this)