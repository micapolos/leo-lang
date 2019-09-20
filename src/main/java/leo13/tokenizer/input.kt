package leo13.tokenizer

import leo13.LeoObject
import leo13.script.lineTo
import leo13.script.script

data class Input(val colon: BooleanColon, val name: String) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "input"
	override val scriptableBody get() = script(colon.scriptableLine, "name" lineTo script(name))
}

fun input(colon: BooleanColon, name: String) = Input(colon, name)
