package leo32

data class Script(
	val string: String)

val String.script
	get() =
		Script(this)
