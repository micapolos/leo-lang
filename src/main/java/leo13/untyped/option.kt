package leo13.untyped

import leo13.script.lineTo
import leo13.script.script
import leo13.script.unsafeOnlyLine
import leo13.scripter.toString

val optionType = leo13.scripter.Scripter(
	"option",
	{ script(name lineTo patternType.bodyScript(rhs)) },
	{ unsafeOnlyLine.run { name optionTo patternType.unsafeBodyValue(rhs) } })

data class Option(val name: String, val rhs: Pattern) {
	override fun toString() = optionType.toString(this)
}

infix fun String.optionTo(rhs: Pattern) = Option(this, rhs)
