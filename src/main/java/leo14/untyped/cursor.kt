package leo14.untyped

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin

data class Cursor(
	val parentOrNull: CursorParent?,
	val value: Value)

data class CursorParent(
	val cursor: Cursor,
	val name: String)

fun Cursor.begin(name: String): Cursor =
	Cursor(CursorParent(this, name), value())

fun Cursor.append(line: Line): Cursor =
	Cursor(parentOrNull, value.plus(line))

fun Cursor.end(): Cursor? =
	parentOrNull?.let { parent ->
		Cursor(
			parent.cursor.parentOrNull,
			parent.cursor.value.plus(parent.name lineTo value))
	}

fun Cursor.updateProgram(fn: Value.() -> Value) =
	copy(value = value.fn())

val Cursor.fragment: Fragment
	get() =
		Fragment(parentOrNull?.fragmentParent, value.script)

val CursorParent.fragmentParent
	get() =
		FragmentParent(cursor.fragment, begin(name))