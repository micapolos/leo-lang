package leo3

import leo.base.*

data class Script(
	val lineStackOrNull: Stack<Line>?) {
	override fun toString() = appendableString { it.append(this) }
}

fun script(vararg lines: Line) =
	Script(stackOrNull(*lines))

fun Appendable.append(script: Script): Appendable =
	fold(script.lineStackOrNull?.reverse.seq, Appendable::append)

fun Script.lineAt(word: Word) =
	lineStackOrNull?.onlyOrNull { it.word == word }

fun Script.scriptAt(word: Word) =
	lineAt(word)?.script

val Script.onlyLine
	get() =
		lineStackOrNull?.onlyOrNull

fun Script.onlyScriptAt(word: Word) =
	onlyLine?.scriptAt(word)
