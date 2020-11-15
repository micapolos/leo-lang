package leo21.type

import leo13.Stack
import leo13.first
import leo13.push
import leo13.stack

inline class Lines(val lineStack: Stack<Line>)

val Stack<Line>.lines get() = Lines(this)

val emptyLines = Lines(stack())

fun Lines.plus(line: Line) = lineStack.push(line).lines

fun Lines.lineOrNull(name: String): Line? =
	lineStack.first { it.name == name }
