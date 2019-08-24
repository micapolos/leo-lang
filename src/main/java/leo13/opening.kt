package leo13

data class Opening(val name: String) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "opening" lineTo script(name lineTo script())
}

fun opening(name: String) = Opening(name)
