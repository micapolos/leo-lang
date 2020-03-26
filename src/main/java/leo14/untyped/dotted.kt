package leo14.untyped

import leo.base.appendableString
import leo.base.iterate
import leo.base.runIf
import leo.base.string
import leo14.*

val Script.dottedString
	get() =
		appendableString { it.appendDotted(this, 0) }

val ScriptLink.dottedString
	get() =
		appendableString { it.appendDotted(this, 0) }

val ScriptLine.dottedString
	get() =
		appendableString { it.appendDotted(this, 0) }

val ScriptField.dottedString
	get() =
		appendableString { it.appendDotted(this, 0) }

fun Appendable.appendDotted(script: Script, indent: Int): Appendable =
	when (script) {
		is UnitScript -> this
		is LinkScript -> appendDotted(script.link, indent)
	}

fun Appendable.appendDotted(link: ScriptLink, indent: Int): Appendable =
	this
		.appendDotted(link.lhs, indent)
		.runIf(!link.lhs.isEmpty) {
			if (link.lhs.isDottedLhs && link.line.isDottedRhs) append(".")
			else append("\n").appendIndent(indent)
		}
		.appendDotted(link.line, indent)

fun Appendable.appendDotted(line: ScriptLine, indent: Int): Appendable =
	when (line) {
		is LiteralScriptLine -> appendDotted(line.literal)
		is FieldScriptLine -> appendDotted(line.field, indent)
	}

fun Appendable.appendDotted(field: ScriptField, indent: Int): Appendable =
	if (field.rhs.isEmpty) append(field.string)
	else if (field.rhs.isSimpleRhs) append(field.string).append(" ").appendDotted(field.rhs, indent)
	else append(field.string).append("\n").appendIndent(indent + 1).appendDotted(field.rhs, indent + 1)

fun Appendable.appendDotted(literal: Literal): Appendable =
	append(literal.string)

fun Appendable.appendIndent(indent: Int): Appendable =
	iterate(indent) { append("  ") }

val Script.isSimpleRhs: Boolean
	get() =
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isSimpleRhs
		}

val ScriptLink.isSimpleRhs: Boolean
	get() =
		isDotted || lhs.isEmpty

val Script.isDottedLhs: Boolean
	get() =
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isDottedLhs
		}

val ScriptLink.isDottedLhs: Boolean
	get() =
		line.isName

val Script.isDotted: Boolean
	get() =
		when (this) {
			is UnitScript -> true
			is LinkScript -> link.isDotted
		}

val ScriptLink.isDotted: Boolean
	get() =
		lhs.isDotted && line.isDottedRhs

val ScriptLine.isDottedRhs: Boolean
	get() =
		isName

val ScriptLine.isName: Boolean
	get() =
		when (this) {
			is LiteralScriptLine -> false
			is FieldScriptLine -> field.isSimple
		}