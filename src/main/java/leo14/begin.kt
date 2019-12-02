package leo14

data class Begin(val string: String) {
	override fun toString() = "$string("
}

fun begin(string: String) = Begin(string)

val Begin.reflectScriptLine
	get() =
		"begin" lineTo script(string)