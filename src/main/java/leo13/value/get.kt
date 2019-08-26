package leo13.value

import leo13.script.lineTo
import leo13.script.script
import leo13.token.nameAsScriptLine

data class Get(val name: String) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "get" lineTo script(name.nameAsScriptLine)
}

fun get(name: String) = Get(name)