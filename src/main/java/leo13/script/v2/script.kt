package leo13.script.v2

import leo.base.fold
import leo13.js.compiler.Number
import leo13.js.compiler.code
import leo13.js.compiler.number

sealed class Script
data class UnitScript(val unit: Unit) : Script()
data class LinkScript(val link: ScriptLink) : Script()

sealed class ScriptLine
data class StringScriptLine(val string: String) : ScriptLine()
data class NumberScriptLine(val number: Number) : ScriptLine()
data class FieldScriptLine(val field: ScriptField) : ScriptLine()

data class ScriptLink(val lhs: Script, val line: ScriptLine)
data class ScriptField(val string: String, val rhs: Script)

fun script(unit: Unit): Script = UnitScript(unit)
fun script(string: String): Script = script(line(string))
fun script(int: Int): Script = script(line(number(int)))
fun script(double: Double): Script = script(line(number(double)))
fun line(string: String): ScriptLine = StringScriptLine(string)
fun line(number: Number): ScriptLine = NumberScriptLine(number)
fun line(field: ScriptField): ScriptLine = FieldScriptLine(field)
fun Script.plus(vararg lines: ScriptLine) = fold(lines) { LinkScript(this linkTo it) }
fun script(vararg lines: ScriptLine): Script = script(Unit).plus(*lines)
fun script(field: ScriptField, vararg fields: ScriptField): Script =
	script(line(field)).fold(fields) { plus(line(it)) }

infix fun String.fieldTo(rhs: Script) = ScriptField(this, rhs)
infix fun String.fieldTo(line: ScriptLine) = fieldTo(script(line))
infix fun String.fieldTo(int: Int) = fieldTo(script(line(number(int))))
infix fun String.fieldTo(double: Double) = fieldTo(script(line(number(double))))
infix fun String.fieldTo(string: String) = fieldTo(script(line(string)))
fun field(string: String) = string fieldTo script()
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)
infix fun String.lineTo(script: Script) = line(fieldTo(script))

val String.code get() = "\"$this\"" // TODO: Escape

val Script.code: String
	get() =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.code
	}

val ScriptLine.code
	get() =
	when (this) {
		is StringScriptLine -> string.code
		is NumberScriptLine -> number.code
		is FieldScriptLine -> field.code
	}

val ScriptLink.code
	get() =
		if (lhs is UnitScript) line.code
		else "${lhs.code}.${line.code}"

val ScriptField.code
	get() =
		"$string(${rhs.code})"
