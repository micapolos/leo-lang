package leo32

import leo.base.Stack

data class Script(
	val entryStack: Stack<Entry>)

val nullScript
	get() =
		null as Script?
