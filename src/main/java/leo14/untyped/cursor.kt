package leo14.untyped

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin

data class Cursor(
	val parentOrNull: CursorParent?,
	val program: Program)

data class CursorParent(
	val cursor: Cursor,
	val name: String)

fun Cursor.begin(name: String): Cursor =
	Cursor(CursorParent(this, name), program())

fun Cursor.append(line: Line): Cursor =
	Cursor(parentOrNull, program.plus(line))

fun Cursor.end(): Cursor? =
	parentOrNull?.let { parent ->
		Cursor(
			parent.cursor.parentOrNull,
			parent.cursor.program.plus(parent.name lineTo program))
	}

fun Cursor.updateProgram(fn: Program.() -> Program) =
	copy(program = program.fn())

val Cursor.fragment: Fragment
	get() =
		Fragment(parentOrNull?.fragmentParent, program.script)

val CursorParent.fragmentParent
	get() =
		FragmentParent(cursor.fragment, begin(name))