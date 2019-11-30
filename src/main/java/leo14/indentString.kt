package leo14

import leo.base.Indent
import leo.base.inc
import leo.base.indent
import leo.base.string

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
