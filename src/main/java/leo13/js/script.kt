package leo13.js

import leo.base.fold
import java.math.BigDecimal

typealias Plain = Nothing

sealed class Script
data class UnitScript(val unit: Unit) : Script()
data class LinkScript(val link: ScriptLink) : Script()

sealed class ScriptLine
data class StringScriptLine(val string: String) : ScriptLine()
data class BigDecimalScriptLine(val bigDecimal: BigDecimal) : ScriptLine()
data class ScriptFieldLine(val field: ScriptField) : ScriptLine()

data class ScriptLink(val lhs: Script, val line: ScriptLine)
data class ScriptField(val string: String, val rhs: Script)

fun script(unit: Unit): Script = UnitScript(unit)
fun line(string: String): ScriptLine = StringScriptLine(string)
fun line(bigDecimal: BigDecimal): ScriptLine = BigDecimalScriptLine(bigDecimal)
fun line(field: ScriptField): ScriptLine = ScriptFieldLine(field)
fun line(int: Int): ScriptLine = line(BigDecimal(int))
fun line(double: Double): ScriptLine = line(BigDecimal(double))
fun Script.plus(vararg lines: ScriptLine) = fold(lines) { LinkScript(this linkTo it) }
fun script(vararg lines: ScriptLine): Script = script(Unit).plus(*lines)
fun script(field: ScriptField, vararg fields: ScriptField): Script =
	script(line(field)).fold(fields) { plus(line(it)) }

infix fun String.fieldTo(rhs: Script) = ScriptField(this, rhs)
infix fun String.fieldTo(int: Int) = fieldTo(script(line(int)))
infix fun String.fieldTo(double: Double) = fieldTo(script(line(double)))
infix fun String.fieldTo(string: String) = fieldTo(script(line(string)))
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)

val Script.code: String
	get() =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.code
	}

val ScriptLine.code
	get() =
	when (this) {
		is StringScriptLine -> "\"$string\""
		is BigDecimalScriptLine -> "$bigDecimal"
		is ScriptFieldLine -> field.code
	}

val ScriptLink.code
	get() =
		if (lhs is UnitScript) "${line.code}"
		else "${lhs.code}.${line.code}"

val ScriptField.code
	get() =
		"$string(${rhs.code})"
