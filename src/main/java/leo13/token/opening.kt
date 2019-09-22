package leo13.token

import leo13.openingName
import leo13.script.lineTo
import leo13.script.script

data class Opening(val name: String) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = openingName lineTo script(name lineTo script())
}

fun opening(name: String) = Opening(name)
