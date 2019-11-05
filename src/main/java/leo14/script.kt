package leo14

import leo.base.fold

sealed class Script

data class UnitScript(val unit: Unit) : Script() {
	override fun toString() = ""
}

data class LinkScript(val link: ScriptLink) : Script() {
	override fun toString() = link.toString()
}

sealed class ScriptLine

data class LiteralScriptLine(val literal: Literal) : ScriptLine() {
	override fun toString() = literal.toString()
}

data class FieldScriptLine(val field: ScriptField) : ScriptLine() {
	override fun toString() = field.toString()
}

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = "$lhsString$line"
	val lhsString = if (lhs is UnitScript) "" else "$lhs."
}

data class ScriptField(val string: String, val rhs: Script) {
	override fun toString() = "$string($rhs)"
}

fun script(unit: Unit): Script = UnitScript(unit)
fun script(string: String): Script = script(line(string))
fun script(link: ScriptLink): Script = LinkScript(link)
fun script(int: Int): Script = script(line(number(int)))
fun script(double: Double): Script = script(line(number(double)))
fun line(literal: Literal): ScriptLine = LiteralScriptLine(literal)
fun line(string: String): ScriptLine = line(literal(string))
fun line(number: Number): ScriptLine = line(literal(number))
fun line(field: ScriptField): ScriptLine = FieldScriptLine(field)
fun Script.plus(vararg lines: ScriptLine) = fold(lines) { LinkScript(this linkTo it) }
fun Script.plus(field: ScriptField, vararg fields: ScriptField): Script = plus(line(field)).fold(fields) { plus(it) }
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
		is LiteralScriptLine -> literal.code
		is LiteralScriptLine -> literal.code
		is FieldScriptLine -> field.code
	}

val Literal.code
	get() =
		when (this) {
			is StringLiteral -> string.code
			is NumberLiteral -> number.code
		}

val ScriptLink.code
	get() =
		if (lhs is UnitScript) line.code
		else "${lhs.code}.${line.code}"

val ScriptField.code
	get() =
		"$string(${rhs.code})"
