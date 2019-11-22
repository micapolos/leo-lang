package leo14

import leo.base.*
import leo13.Stack
import leo13.fold
import leo13.reverse

sealed class Script {
	override fun toString() = string(0.indent, defaultIndentConfig)
}

data class UnitScript(val unit: Unit) : Script() {
	override fun toString() = super.toString()
}

data class LinkScript(val link: ScriptLink) : Script() {
	override fun toString() = super.toString()
}

sealed class ScriptLine {
	override fun toString() = string(0.indent, defaultIndentConfig)
}

data class LiteralScriptLine(val literal: Literal) : ScriptLine() {
	override fun toString() = super.toString()
}

data class FieldScriptLine(val field: ScriptField) : ScriptLine() {
	override fun toString() = super.toString()
}

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = string(0.indent, defaultIndentConfig)
}

data class ScriptField(val string: String, val rhs: Script) {
	override fun toString() = string(0.indent, defaultIndentConfig)
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

val Script.isWord
	get() =
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isWord
		}

val ScriptLink.isWord
	get() =
		lhs.isEmpty && line.isWord

val ScriptLine.isWord
	get() =
		when (this) {
			is LiteralScriptLine -> true
			is FieldScriptLine -> field.rhs.isEmpty
		}

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

val Script.isSingleLine: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.isSingleLine
		}

val ScriptLink.isSingleLine
	get() =
		lhs.isEmpty

val Script.hasWordsOnly: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.hasWordsOnly
		}

val ScriptLink.hasWordsOnly
	get() =
		lhs.hasWordsOnly && line.hasWordsOnly

val ScriptLine.hasWordsOnly
	get() =
		when (this) {
			is LiteralScriptLine -> true
			is FieldScriptLine -> field.rhs.isEmpty
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

val Script.indentString get() = string(0.indent, defaultIndentConfig)

fun Script.string(indent: Indent, config: IndentConfig): String =
	when (this) {
		is UnitScript -> ""
		is LinkScript ->
			if (config.maxLength == 0) "...${link.lineCount} more..."
			else if (config.maxDepth == 0) "..."
			else link.string(indent, config)
	}

fun ScriptLink.string(indent: Indent, config: IndentConfig): String =
	when (lhs) {
		is UnitScript -> line.string(indent, config)
		is LinkScript ->
			lhs.string(indent, config.next) + "\n" + indent.string + line.string(indent, config)
	}

fun ScriptLine.string(indent: Indent, config: IndentConfig): String =
	when (this) {
		is LiteralScriptLine -> literal.toString()
		is FieldScriptLine -> field.string(indent, config)
	}

fun ScriptField.string(indent: Indent, config: IndentConfig): String =
	when (rhs) {
		is UnitScript -> string
		is LinkScript ->
			if (rhs.isSingleLine || config.maxDepth <= 1) "$string: ${rhs.string(indent, config.begin)}"
			else "$string\n${indent.inc.string}${rhs.string(indent.inc, config.beginDeep)}"
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

// === Line count

val Script.lineSeq: Seq<ScriptLine>
	get() =
		when (this) {
			is UnitScript -> emptySeq()
			is LinkScript -> seq { link.lineSeqNode }
		}

val ScriptLink.lineSeqNode: SeqNode<ScriptLine>
	get() =
		line then lhs.lineSeq

val Script.lineCount
	get() =
		0.fold(lineSeq) { inc() }

val ScriptLink.lineCount
	get() =
		0.fold(lineSeqNode) { inc() }
