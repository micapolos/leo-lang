package leo3

import leo.base.appendableString
import leo.base.notNullIf

data class Line(
	val word: Word,
	val script: Script) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(word: Word, scriptOrNull: Script? = null) =
	Line(word, scriptOrNull ?: script())

fun Appendable.append(line: Line): Appendable =
	this
		.append(line.word)
		.append('(')
		.append(line.script)
		.append(')')

fun Line.scriptAt(word: Word) =
	notNullIf(this.word == word) { script }