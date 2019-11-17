package leo14

import leo.base.*
import leo13.Stack
import leo13.fold
import leo13.reverse

sealed class Script {
	override fun toString() = string(0.indent)
}

data class UnitScript(val unit: Unit) : Script() {
	override fun toString() = super.toString()
}

data class LinkScript(val link: ScriptLink) : Script() {
	override fun toString() = super.toString()
}

sealed class ScriptLine {
	override fun toString() = string(0.indent)
}

data class LiteralScriptLine(val literal: Literal) : ScriptLine() {
	override fun toString() = super.toString()
}

data class FieldScriptLine(val field: ScriptField) : ScriptLine() {
	override fun toString() = super.toString()
}

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = string(0.indent)
}

data class ScriptField(val string: String, val rhs: Script) {
	override fun toString() = string(0.indent)
}

fun script(unit: Unit): Script = UnitScript(unit)
fun script(literal: Literal): Script = script(line(literal))
fun script(link: ScriptLink): Script = LinkScript(link)
fun line(string: String): ScriptLine = line(field(string))
fun line(literal: Literal): ScriptLine = LiteralScriptLine(literal)
fun line(field: ScriptField): ScriptLine = FieldScriptLine(field)
fun Script.plus(vararg lines: ScriptLine) = fold(lines) { LinkScript(this linkTo it) }
fun Script.plus(field: ScriptField, vararg fields: ScriptField): Script = plus(line(field)).fold(fields) { plus(it) }
fun script(vararg lines: ScriptLine): Script = script(Unit).plus(*lines)
fun script(string: String, vararg strings: String) = script(field(string)).fold(strings) { plus(field(it)) }
fun script(field: ScriptField, vararg fields: ScriptField): Script =
	script(line(field)).fold(fields) { plus(line(it)) }

infix fun String.fieldTo(rhs: Script) = ScriptField(this, rhs)
infix fun String.fieldTo(line: ScriptLine) = fieldTo(script(line))
infix fun String.fieldTo(literal: Literal) = fieldTo(script(literal))
fun field(string: String) = string fieldTo script()
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)
infix fun String.lineTo(script: Script) = line(fieldTo(script))

val Script.isEmpty get() = (this is UnitScript)

val Script.isSimple: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.isSimple
		}

val ScriptLink.isSimple
	get() =
		lhs.isSimple && line.isSimple

val ScriptLine.isSimple
	get() =
		when (this) {
			is LiteralScriptLine -> true
			is FieldScriptLine -> field.isSimple
		}

val ScriptField.isSimple get() = rhs.isEmpty

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

fun <V> Stack<V>.script(fn: V.() -> ScriptLine): Script =
	script().fold(reverse) { plus(it.fn()) }

// === Indented string

val Script.indentString get() = string(0.indent)

fun Script.string(indent: Indent): String =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.string(indent)
	}

fun ScriptLink.string(indent: Indent): String =
	when (lhs) {
		is UnitScript -> line.string(indent)
		is LinkScript ->
			if (lhs.isSimple) lhs.string(indent) + " " + line.string(indent)
			else lhs.string(indent) + "\n" + indent.string + line.string(indent)
	}

fun ScriptLine.string(indent: Indent): String =
	when (this) {
		is LiteralScriptLine -> literal.toString()
		is FieldScriptLine -> field.string(indent)
	}

fun ScriptField.string(indent: Indent): String =
	when (rhs) {
		is UnitScript -> string
		is LinkScript ->
			if (rhs.link.lhs.isEmpty || rhs.link.isSimple) "$string: ${rhs.string(indent)}"
			else "$string\n${indent.inc.string}${rhs.string(indent.inc)}"
	}

// == Core string

val Script.coreString: String
	get() =
		when (this) {
			is UnitScript -> ""
			is LinkScript -> link.coreString
		}

val ScriptLink.coreString: String
	get() =
		if (lhs.isEmpty) line.coreString
		else "${lhs.coreString} ${line.coreString}"

val ScriptLine.coreString: String
	get() =
		when (this) {
			is LiteralScriptLine -> literal.toString()
			is FieldScriptLine -> field.coreString
		}

val ScriptField.coreString: String
	get() =
		"$string(${rhs.coreString})"
