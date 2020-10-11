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
val ScriptLine.script get() = script(this)

val emptyScript: Script = UnitScript(Unit)
val String.scriptLine get() = line(this)

fun link(line: ScriptLine, vararg lines: ScriptLine) =
	ScriptLink(script(), line).fold(lines) { ScriptLink(script(this), it) }

val Literal.line get() = line(this)

fun Script.plus(script: Script): Script =
	when (script) {
		is UnitScript -> this
		is LinkScript -> plus(script.link.lhs).plus(script.link.line)
	}

infix fun String.fieldTo(rhs: Script) = ScriptField(this, rhs)
infix fun String.fieldTo(line: ScriptLine) = fieldTo(script(line))
infix fun String.fieldTo(literal: Literal) = fieldTo(script(literal))
fun field(string: String) = string fieldTo script()
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)
infix fun String.lineTo(script: Script) = line(fieldTo(script))
val String.line: ScriptLine get() = this lineTo script()
val Stack<ScriptLine>.script get() = emptyScript.fold(reverse) { plus(it) }

val Script.isEmpty get() = (this is UnitScript)
val Script.linkOrNull get() = (this as? LinkScript)?.link

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

val Script.forget
	get() =
		script()

fun Script.replaceWith(script: Script) =
	script

val Script.headOrNull
	get() =
		when (this) {
			is UnitScript -> null
			is LinkScript -> script(link.line)
		}

val Script.tailOrNull
	get() =
		when (this) {
			is UnitScript -> null
			is LinkScript -> link.lhs
		}

val Script.bodyOrNull
	get() =
		when (this) {
			is UnitScript -> null
			is LinkScript -> link.line.bodyOrNull
		}

val ScriptLine.bodyOrNull
	get() =
		when (this) {
			is LiteralScriptLine -> null
			is FieldScriptLine -> field.rhs
		}

val Script.head
	get() =
		headOrNull ?: plus("head" lineTo script())

val Script.tail
	get() =
		tailOrNull ?: plus("tail" lineTo script())

val Script.body
	get() =
		bodyOrNull ?: plus("body" lineTo script())

fun Script.make(string: String) =
	script(string lineTo this)

operator fun Script.get(string: String) =
	when (this) {
		is UnitScript -> null
		is LinkScript -> link[string]
	}

operator fun ScriptLink.get(string: String) =
	when (lhs) {
		is UnitScript -> line[string]
		is LinkScript -> null
	}

operator fun ScriptLine.get(string: String) =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> field.rhs.lineOrNull(string)?.let { line -> script(line) }
	}

fun Script.rhsOrNull(string: String): Script? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> link.rhsOrNull(string)
	}

fun ScriptLink.rhsOrNull(string: String) =
	line.rhsOrNull(string) ?: lhs.rhsOrNull(string)

fun ScriptLine.rhsOrNull(string: String) =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> field.rhsOrNull(string)
	}

fun ScriptField.rhsOrNull(string: String) =
	if (this.string == string) rhs
	else null

fun Script.lineOrNull(string: String): ScriptLine? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> link.lineOrNull(string)
	}

fun ScriptLink.lineOrNull(string: String) =
	line.ifNamed(string) ?: lhs.lineOrNull(string)

fun ScriptLine.ifNamed(string: String) =
	if (isNamed(string)) this
	else null

fun ScriptLine.isNamed(string: String) =
	when (this) {
		is LiteralScriptLine -> literal.name == string
		is FieldScriptLine -> field.string == string
	}

val Literal.name
	get() =
		when (this) {
			is NumberLiteral -> "number"
			is StringLiteral -> "text"
		}

fun leo(vararg lines: Any): Script =
	script(*lines.map { it.anyLine }.toTypedArray())

operator fun String.invoke(vararg lines: Any): ScriptLine =
	if (lines.size == 1 && lines[0] is Script) this lineTo (lines[0] as Script)
	else this lineTo leo(*lines)

val Any.anyLine: ScriptLine
	get() =
		when (this) {
			is Int -> line(literal(this))
			is Double -> line(literal(this))
			is String -> line(literal(this))
			is ScriptLine -> this
			else -> error("$this.anyLine")
		}

val Script.reflectScriptLine
	get() =
		"script" lineTo this

val ScriptLine.fieldOrNull: ScriptField?
	get() =
		(this as? FieldScriptLine)?.field

val ScriptLink.onlyLineOrNull: ScriptLine?
	get() =
		notNullIf(lhs.isEmpty) { line }

val ScriptField.onlyStringOrNull: String?
	get() =
		notNullIf(rhs.isEmpty) { string }

val Script.onlyStringOrNull: String?
	get() =
		linkOrNull?.onlyLineOrNull?.fieldOrNull?.onlyStringOrNull
