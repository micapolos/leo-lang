package leo3

import leo.base.*

data class Script(
	val lineStack: Stack<Line>) {
	override fun toString() = appendableString { it.append(this) }
}

fun script(line: Line, vararg lines: Line) =
	Script(stack(line, *lines))

fun Appendable.append(script: Script): Appendable =
	fold(script.lineStack.reverse.seq, Appendable::append)
