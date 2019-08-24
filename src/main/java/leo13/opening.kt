package leo13

data class Opening(val name: String) {
	override fun toString() = asScript.toString()
	val asScript get() = script(name lineTo script())
}

fun opening(name: String) = Opening(name)
