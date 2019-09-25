package leo13.interpreter

import leo13.ObjectScripting
import leo13.interpretedName
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type
import leo13.type.type
import leo13.value.Value
import leo13.value.scriptLine
import leo13.value.value

data class ValueTyped(val value: Value, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() = interpretedName lineTo script(
			value.scriptLine, type.scriptingLine)
}

fun typed(value: Value, type: Type) = ValueTyped(value, type)
fun valueTyped() = typed(value(), type())