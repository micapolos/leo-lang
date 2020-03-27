package leo14.untyped

import leo.base.*
import leo14.*

val Script.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

val ScriptLink.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

val ScriptLine.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

val ScriptField.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

val Fragment.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

val FragmentParent.dottedString
	get() =
		appendableIndentedString { it.appendDotted(this) }

fun AppendableIndented.appendDotted(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> appendDotted(script.link)
	}

fun AppendableIndented.appendDotted(link: ScriptLink): AppendableIndented =
	this
		.appendDotted(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.lhs.isDottedLhs && link.line.isDottedRhs) append(".")
			else append("\n")
		}
		.appendDotted(link.line)

fun AppendableIndented.appendDotted(line: ScriptLine): AppendableIndented =
	when (line) {
		is LiteralScriptLine -> appendDotted(line.literal)
		is FieldScriptLine -> appendDotted(line.field)
	}

fun AppendableIndented.appendDotted(field: ScriptField): AppendableIndented =
	if (field.rhs.isEmpty) append(field.string)
	else if (field.rhs.isSimpleRhs) append(field.string).append(" ").appendDotted(field.rhs)
	else append(field.string).indented { append("\n").appendDotted(field.rhs) }

fun AppendableIndented.appendDotted(literal: Literal): AppendableIndented =
	append(literal.string)

fun AppendableIndented.appendDotted(fragment: Fragment): AppendableIndented =
	this
		.run {
			if (fragment.parent == null) this
			else {
				this
					.appendDotted(fragment.parent)
					.run {
						if (fragment.script.isEmpty) this//append(" ")
						else indented.append("\n")
					}
			}
		}
		.appendDotted(fragment.script)
		.run {
			if (fragment.parent == null)
				if (fragment.script.isEmpty) this
				else append("\n")
			else {
				if (fragment.script.isEmpty) append(" ")
				else append("\n")
			}
		}

fun AppendableIndented.appendDotted(parent: FragmentParent): AppendableIndented =
	this
		.appendDotted(parent.fragment)
		.run {
			if (parent.fragment.script.isEmpty) this
			else if (parent.fragment.script.isDottedLhs) this
			else this//append("\n?")
		}
		.append(parent.begin.string)


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
		line.isSimple

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
