package leo13.value

import leo13.Scriptable
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.token.scriptableLine

data class ValueLine(
	val name: String,
	val rhs: Value) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name.scriptableLine, rhs.scriptableLine)
}

fun valueLine(name: String, rhs: Value = value()) = ValueLine(name, rhs)
infix fun String.lineTo(value: Value) = valueLine(this, value)

val ScriptLine.valueLine: ValueLine get() = name lineTo rhs.value
val ValueLine.scriptLineOrNull: ScriptLine? get() = rhs.scriptOrNull?.let { name lineTo it }