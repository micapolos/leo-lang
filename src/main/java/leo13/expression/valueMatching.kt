package leo13.expression

import leo13.ObjectScripting
import leo13.matchingName
import leo13.script.lineTo
import leo13.script.script
import leo13.value.Value
import leo13.value.lineTo
import leo13.value.plus
import leo13.value.scriptLine

data class ValueMatching(val value: Value) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			matchingName lineTo script(value.scriptLine)

	fun plus(value: Value) =
		ValueMatching(this.value.plus(matchingName lineTo value))
}

fun matching(value: Value) = ValueMatching(value)