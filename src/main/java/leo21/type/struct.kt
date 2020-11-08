package leo21.type

import leo.base.fold
import leo13.Link
import leo13.Stack
import leo13.any
import leo13.linkOrNull
import leo13.linkTo
import leo13.push
import leo13.stack

data class Struct(val lineStack: Stack<Line>)

val Stack<Line>.struct get() = Struct(this)
val emptyStruct = Struct(stack())

fun Struct.plus(line: Line) =
	if (!allowDuplicateFields && lineStack.any { name == line.name }) error("duplicate field")
	else Struct(lineStack.push(line))

fun struct(vararg lines: Line) = emptyStruct.fold(lines) { plus(it) }

val Struct.linkOrNull: Link<Struct, Line>?
	get() =
		lineStack.linkOrNull?.run { Struct(stack) linkTo value }

