package leo25

import leo.base.appendableString
import leo.base.indentString
import leo14.*
import leo14.matching.name

val Script.string get() = appendableString { it.append(0, this) }.addTrailingNewline

val String.preprocess: String
	get() =
		this
			.convertTabsToSpaces
			.lines()
			.map { it.trimEnd() }
			.joinToString("\n")
			.removeEmptyLines
			.addTrailingNewline

val String.removeEmptyLines get() = replace(Regex("(?m)^[ \t]*\r?\n"), "")

val String.convertTabsToSpaces get() = replace("\t", "  ")

val String.addTrailingNewline
	get() =
		if (isEmpty() || this[length - 1] == '\n') this
		else plus('\n')

fun Appendable.append(indent: Int, script: Script): Appendable =
	when (script) {
		is LinkScript -> append(indent, script.link)
		is UnitScript -> this
	}

fun Appendable.append(indent: Int, link: ScriptLink): Appendable =
	append(indent, link.lhs).append(indent, link.line)

fun Appendable.append(indent: Int, line: ScriptLine): Appendable =
	append(indent.indentString).indentedAppend(indent, line)

fun Appendable.indentedAppend(indent: Int, line: ScriptLine): Appendable =
	when (line) {
		is FieldScriptLine -> indentedAppend(indent, line.field)
		is LiteralScriptLine -> append(line.literal).append("\n")
	}

fun Appendable.indentedAppend(indent: Int, field: ScriptField): Appendable =
	append(field.name).indentedAppendRhs(indent, field.rhs)

fun Appendable.indentedAppendRhs(indent: Int, rhs: Script): Appendable =
	rhs.onlyLineOrNull.let { onlyLineOrNull ->
		if (onlyLineOrNull == null) append("\n").append(indent.inc(), rhs)
		else append(" ").indentedAppendRhs(indent, onlyLineOrNull)
	}

fun Appendable.indentedAppendRhs(indent: Int, rhs: ScriptLine): Appendable =
	when (rhs) {
		is FieldScriptLine -> indentedAppend(indent, rhs.field)
		is LiteralScriptLine -> append(rhs.literal).append("\n")
	}

fun Appendable.append(literal: Literal): Appendable =
	when (literal) {
		is NumberLiteral -> append(literal.number.toString())
		is StringLiteral -> append(literal.string.literalString)
	}
