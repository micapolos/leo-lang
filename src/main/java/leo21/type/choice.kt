package leo21.type

import leo.base.fold
import leo13.Link
import leo13.Stack
import leo13.any
import leo13.linkOrNull
import leo13.linkTo
import leo13.push
import leo13.stack

data class Choice(val lineStack: Stack<Line>)

val emptyChoice = Choice(stack())

fun Choice.plus(line: Line) =
	if (!allowDuplicateFields && lineStack.any { name == line.name }) error("duplicate case")
	else Choice(lineStack.push(line))

fun choice(vararg lines: Line) = emptyChoice.fold(lines) { plus(it) }

val Choice.linkOrNull: Link<Choice, Line>?
	get() =
		lineStack.linkOrNull?.run { Choice(stack) linkTo value }