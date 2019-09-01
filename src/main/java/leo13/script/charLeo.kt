package leo13.script

import leo13.LeoObject

data class CharLeo(val char: Char) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "char"
	override val scriptableBody get() = nameScript
}

val CharLeo.nameScript: Script
	get() =
		when (char) {
			'(' -> script("opening" lineTo script("bracket"))
			')' -> script("closing" lineTo script("bracket"))
			' ' -> script("space")
			':' -> script("colon")
			'\n' -> script("newline")
			'\t' -> script("tab")
			// TODO: Escape all!!!
			else -> script("$char")
		}

fun leo(char: Char) = CharLeo(char)
