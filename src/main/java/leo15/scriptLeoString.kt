package leo15

import leo.base.*
import leo14.*

val useDots = false

val Script.string
	get() =
		appendableIndentedString { it.append(this) }

val ScriptLink.string
	get() =
		appendableIndentedString { it.append(this) }

val ScriptLine.string
	get() =
		appendableIndentedString { it.append(this) }

val ScriptField.string
	get() =
		appendableIndentedString { it.append(this) }

val Fragment.string
	get() =
		appendableIndentedString { it.append(this) }

val Fragment.stringNonTail
	get() =
		appendableIndentedString { it.appendNonTail(this) }

val FragmentParent.string
	get() =
		appendableIndentedString { it.append(this) }

fun AppendableIndented.append(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> append(script.link)
	}

fun AppendableIndented.append(link: ScriptLink): AppendableIndented =
	appendNonTail(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.isDottable) append(".")
			else append("\n")
		}
		.append(link.line)

fun AppendableIndented.appendNonTail(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> appendNonTail(script.link)
	}

fun AppendableIndented.appendNonTail(link: ScriptLink): AppendableIndented =
	appendNonTail(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.isDottableNonTail) append(".")
			else append("\n")
		}
		.append(link.line)

fun AppendableIndented.append(line: ScriptLine): AppendableIndented =
	when (line) {
		is LiteralScriptLine -> append(line.literal)
		is FieldScriptLine -> append(line.field)
	}

fun AppendableIndented.append(field: ScriptField): AppendableIndented =
	if (field.rhs.isEmpty) append(field.string)
	else if (field.isSpaceable) append(field.string).append(" ").append(field.rhs)
	else append(field.string).indented { append("\n").append(field.rhs) }

fun AppendableIndented.append(literal: Literal): AppendableIndented =
	append(literal.string)

fun AppendableIndented.append(fragment: Fragment): AppendableIndented =
	this
		.ifNotNull(fragment.parent) { append(it) }
		.append(fragment.script)
		.runIf(!fragment.script.isEmpty) { append("\n") }

fun AppendableIndented.appendNonTail(fragment: Fragment): AppendableIndented =
	this
		.ifNotNull(fragment.parent) { append(it) }
		.appendNonTail(fragment.script)
		.runIf(!fragment.script.isEmpty) { append("\n") }

fun AppendableIndented.append(parent: FragmentParent): AppendableIndented =
	this
		.appendNonTail(parent.fragment)
		.append(parent.begin.string)
		.indented.append("\n")

val ScriptField.isSpaceable: Boolean
	get() =
		rhs.isSpaceableRhs

val Script.isSpaceableRhs: Boolean
	get() =
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isSpaceableRhs
		}

val ScriptLink.isSpaceableRhs: Boolean
	get() =
		lhs.isEmpty || lhs.isSpaceableRhs && isDottable

val ScriptLink.isDottable: Boolean
	get() =
		lhs.isDottableLhs && line.isDottableRhs

val ScriptLink.isDottableNonTail: Boolean
	get() =
		isDottable && line.isWord

val Script.isDottableLhs: Boolean
	get() =
		useDots && when (this) {
			is UnitScript -> false
			is LinkScript -> link.isDottableLhs
		}

val ScriptLink.isDottableLhs: Boolean
	get() =
		useDots && line.isSimple

val ScriptLine.isDottableRhs: Boolean
	get() =
		when (this) {
			is LiteralScriptLine -> false
			is FieldScriptLine -> true
		}

val Script.isDotted: Boolean
	get() =
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isDotted
		}

val ScriptLink.isDotted: Boolean
	get() =
		lhs.isDotted && line.isDottableRhs
