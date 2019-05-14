package leo3

import leo.base.appendableString
import leo.base.ifNotNull

data class Line(
	val word: Word,
	val scriptOrNull: Script?) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(word: Word, scriptOrNull: Script? = null) =
	Line(word, scriptOrNull)

fun Appendable.append(line: Line): Appendable =
	this
		.append(line.word)
		.append('(')
		.ifNotNull(line.scriptOrNull, Appendable::append)
		.append(')')

