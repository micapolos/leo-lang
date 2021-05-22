package leo25

import leo14.Script
import leo14.fullIndentString

val Script.string get() = fullIndentString.addTrailingNewline

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
		else this + '\n'