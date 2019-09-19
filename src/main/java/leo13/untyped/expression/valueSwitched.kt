package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.value.Value
import leo13.untyped.value.lineTo
import leo13.untyped.value.plus
import leo13.untyped.value.scriptLine

data class ValueSwitched(val value: Value) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"switched" lineTo script(value.scriptLine)

	fun plus(value: Value) =
		ValueSwitched(value.plus("switched" lineTo value))
}

fun switched(value: Value) = ValueSwitched(value)