package leo14.untyped

import leo.base.*
import leo14.*

val givenDot = false

val Script.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

val ScriptLink.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

val ScriptLine.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

val ScriptField.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

val Fragment.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

val Fragment.leoStringNonTail
	get() =
		appendableIndentedString { it.leoAppendNonTail(this) }

val FragmentParent.leoString
	get() =
		appendableIndentedString { it.leoAppend(this) }

fun AppendableIndented.leoAppend(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> leoAppend(script.link)
	}

fun AppendableIndented.leoAppend(link: ScriptLink): AppendableIndented =
	leoAppendGivenOrNull(link) ?: this
		.leoAppendNonTail(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.isDottable) append(".")
			else append("\n")
		}
		.leoAppend(link.line)

fun AppendableIndented.leoAppendGivenOrNull(link: ScriptLink): AppendableIndented? =
	ifOrNull(givenDot) {
		if (link.lhs == script(givenName)) append(".").leoAppend(link.line)
		else if (link == script() linkTo line(givenName fieldTo script())) append(".")
		else null
	}

fun AppendableIndented.leoAppendNonTail(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> leoAppendNonTail(script.link)
	}

fun AppendableIndented.leoAppendNonTail(link: ScriptLink): AppendableIndented =
	leoAppendGivenOrNull(link) ?: this
		.leoAppendNonTail(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.isDottableNonTail) append(".")
			else append("\n")
		}
		.leoAppend(link.line)

fun AppendableIndented.leoAppend(line: ScriptLine): AppendableIndented =
	when (line) {
		is LiteralScriptLine -> leoAppend(line.literal)
		is FieldScriptLine -> leoAppend(line.field)
	}

fun AppendableIndented.leoAppend(field: ScriptField): AppendableIndented =
	if (field.rhs.isEmpty) append(field.string)
	else if (field.isSpaceable) append(field.string).append(" ").leoAppend(field.rhs)
	else append(field.string).indented { append("\n").leoAppend(field.rhs) }

fun AppendableIndented.leoAppend(literal: Literal): AppendableIndented =
	append(literal.string)

fun AppendableIndented.leoAppend(fragment: Fragment): AppendableIndented =
	this
		.ifNotNull(fragment.parent) { leoAppend(it) }
		.leoAppend(fragment.script)
		.runIf(!fragment.script.isEmpty) { append("\n") }

fun AppendableIndented.leoAppendNonTail(fragment: Fragment): AppendableIndented =
	this
		.ifNotNull(fragment.parent) { leoAppend(it) }
		.leoAppendNonTail(fragment.script)
		.runIf(!fragment.script.isEmpty) { append("\n") }

fun AppendableIndented.leoAppend(parent: FragmentParent): AppendableIndented =
	this
		.leoAppendNonTail(parent.fragment)
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
		when (this) {
			is UnitScript -> false
			is LinkScript -> link.isDottableLhs
		}

val ScriptLink.isDottableLhs: Boolean
	get() =
		line.isSimple

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
