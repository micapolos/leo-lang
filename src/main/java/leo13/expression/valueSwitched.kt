package leo13.expression

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.value.Value
import leo13.value.lineTo
import leo13.value.plus
import leo13.value.scriptLine

data class ValueSwitched(val value: Value) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"switched" lineTo script(value.scriptLine)

	fun plus(value: Value) =
		ValueSwitched(this.value.plus("switched" lineTo value))
}

fun switched(value: Value) = ValueSwitched(value)