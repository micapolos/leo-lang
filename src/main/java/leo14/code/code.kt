package leo14.code

import leo14.lineTo
import leo14.literal
import leo14.script

data class Code(val string: String) {
	override fun toString() = string
}

val String.code get() = Code(this)

val Code.reflectScriptLine
	get() =
		"code" lineTo script(literal(string))