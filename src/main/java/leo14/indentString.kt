package leo14

import leo.base.Indent
import leo.base.inc
import leo.base.indent
import leo.base.string
import kotlin.math.max

data class IndentConfig(
	val maxDeepDepth: Int,
	val maxDepth: Int,
	val maxLength: Int,
	val initialMaxLength: Int)

val IndentConfig.begin
	get() =
		copy(maxDepth = max(0, maxDepth.dec()), maxLength = initialMaxLength)

val IndentConfig.beginDeep
	get() =
		copy(maxDeepDepth = max(0, maxDeepDepth.dec())).begin

val IndentConfig.next
	get() =
		copy(maxLength = max(0, maxLength.dec()))

val defaultIndentConfig = IndentConfig(3, 8, 8, 8)

val Fragment.indent: Indent
	get() =
		parent?.indent ?: 0.indent

val FragmentParent.indent: Indent
	get() =
		fragment.indent.inc

val Fragment.indentString
	get() =
		string(defaultIndentConfig)

fun Fragment.string(config: IndentConfig): String =
	if (parent == null)
		if (script.isEmpty) ""
		else script.string(0.indent, config) + "\n"
	else
		if (script.isEmpty) parent.string(config) + "\n" + indent.string
		else parent.string(config) + "\n" + indent.string + script.string(indent, config) + "\n" + indent.string

fun FragmentParent.string(config: IndentConfig) =
	fragment.string(config) + begin.string
