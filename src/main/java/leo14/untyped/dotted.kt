package leo14.untyped

import leo.ansi
import leo.base.*
import leo.magenta
import leo.reset
import leo14.*

val useDots = true
val dottedColorsParameter = parameter(true)

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

val Tokenizer.leoStringNonTail
	get() =
		appendableIndentedString { it.leoAppendNonTail(this) }

fun AppendableIndented.leoAppend(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> leoAppend(script.link)
	}

fun AppendableIndented.leoAppend(link: ScriptLink): AppendableIndented =
	leoAppendNonTail(link.lhs)
		.runIf(!link.lhs.isEmpty) {
			if (link.isDottable) append(".")
			else append("\n")
		}
		.leoAppend(link.line)

fun AppendableIndented.leoAppendNonTail(script: Script): AppendableIndented =
	when (script) {
		is UnitScript -> this
		is LinkScript -> leoAppendNonTail(script.link)
	}

fun AppendableIndented.leoAppendNonTail(link: ScriptLink): AppendableIndented =
	leoAppendNonTail(link.lhs)
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
	leoAppendEllipsized(literal.toString())

val String.colored
	get() =
		"${ansi.magenta}${this}${ansi.reset}"

const val stringEllipsizedHalfLength = 50

val String.maybeColored
	get() =
		if (dottedColorsParameter.value) colored
		else this

fun AppendableIndented.leoAppendEllipsized(string: String): AppendableIndented =
	if (string.length <= stringEllipsizedHalfLength * 2) append(string.maybeColored)
	else append(string.substring(0, stringEllipsizedHalfLength).maybeColored)
		.append("...")
		.append(string.substring(string.length - stringEllipsizedHalfLength).maybeColored)

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

fun AppendableIndented.leoAppend(tokenizer: Tokenizer): AppendableIndented =
	this
		.ifNotNull(tokenizer.parentOrNull) { leoAppend(it) }
		.leoAppend(tokenizer.script)
		.runIf(!tokenizer.script.isEmpty) {
			if (tokenizer.dotted) append(".")
			else append("\n")
		}

fun AppendableIndented.leoAppendNonTail(tokenizer: Tokenizer): AppendableIndented =
	this
		.ifNotNull(tokenizer.parentOrNull) { leoAppend(it) }
		.leoAppendNonTail(tokenizer.script)
		.runIf(!tokenizer.script.isEmpty) {
			if (tokenizer.dotted) append(".")
			else append("\n")
		}

fun AppendableIndented.leoAppend(parent: TokenizerParent): AppendableIndented =
	this
		.leoAppendNonTail(parent.tokenizer)
		.append(parent.name)
		.run {
			if (parent.newline) indented.append("\n")
			else append(".")
		}

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
			is FieldScriptLine -> field.isSimple
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
