package leo13.script

import leo13.lineTo
import leo13.nameAsScriptLine
import leo13.script

data class Get(val name: String) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "get" lineTo script(name.nameAsScriptLine)
}

fun get(name: String) = Get(name)