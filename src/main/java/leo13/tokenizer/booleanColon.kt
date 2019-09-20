package leo13.tokenizer

import leo13.LeoObject
import leo13.script.script

data class BooleanColon(val boolean: Boolean) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "colon"
	override val scriptableBody get() = script(if (boolean) "yes" else "no")
}

fun colon(boolean: Boolean) = BooleanColon(boolean)
