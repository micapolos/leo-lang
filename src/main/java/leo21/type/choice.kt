package leo21.type

import leo.base.fold
import leo13.Link
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo13.linkTo
import leo13.push
import leo13.stack

data class Choice(val lineStack: Stack<Line>) : AsLine {
	override val asLine get() = line(this)
}

val Stack<Line>.choice get() = Choice(this)
val emptyChoice = Choice(stack())

fun Choice.plus(line: Line) =
	Choice(lineStack.push(line))

fun choice(vararg lines: Line) = emptyChoice.fold(lines) { plus(it) }

val Choice.linkOrNull: Link<Choice, Line>?
	get() =
		lineStack.linkOrNull?.run { Choice(stack) linkTo value }

inline val Link<Choice, Line>.choice get() = tail
inline val Link<Choice, Line>.line get() = head

inline val Choice.isEmpty: Boolean get() = lineStack.isEmpty