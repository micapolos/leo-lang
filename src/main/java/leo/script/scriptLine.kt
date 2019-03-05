package leo.script

import leo.Word
import leo.base.appendableString
import leo.base.ifNotNull
import leo.scriptAppend

data class ScriptLine(
	val word: Word,
	val rhs: Script? = null) {
	override fun toString() = appendableString { it.append(this) }
}

infix fun Word.lineTo(rhs: Script?) =
	ScriptLine(this, rhs)

val Word.line
	get() =
		this lineTo null

fun Appendable.append(line: ScriptLine): Appendable =
	this
		.scriptAppend(line.word)
		.ifNotNull(line.rhs, Appendable::append)
		.append(' ')


