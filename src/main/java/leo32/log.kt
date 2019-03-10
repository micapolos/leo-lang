package leo32

data class Log(
	val tag: Tag)

val Tag.log
	get() =
		Log(this)