package leo13.untyped.evaluator

import leo.base.notNullIf
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.lineName

data class ValueLine(val name: String, val rhs: Value) {
	override fun toString() = scriptLine.toString()
}

infix fun String.lineTo(rhs: Value) = ValueLine(this, rhs)

fun ValueLine.rhsOrNull(selectedName: String): Value? =
	notNullIf(name == selectedName) { rhs }

fun ValueLine.replaceOrNull(line: ValueLine): ValueLine? =
	notNullIf(name == line.name) { line }

val ValueLine.scriptLine: ScriptLine get() =
	lineName lineTo script(bodyScriptLine)

val ValueLine.bodyScriptLine: ScriptLine get() =
	name lineTo rhs.bodyScript