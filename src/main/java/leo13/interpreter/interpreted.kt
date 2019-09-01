package leo13.interpreter

import leo13.Scriptable
import leo13.script.script
import leo13.type.Type
import leo13.type.type
import leo13.value.Value
import leo13.value.value

data class Interpreted(val value: Value, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "interpreted"
	override val scriptableBody get() = script(value.scriptableLine, type.scriptableLine)
}

fun interpreted(value: Value, type: Type) = Interpreted(value, type)
fun interpreted() = interpreted(value(), type())
