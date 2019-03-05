package leo.script

import leo.Word
import leo.base.appendableString
import leo.base.ifNotNull

data class Script(
	val lhs: Script?,
	val term: ScriptLine) {
	override fun toString() = appendableString { it.append(this) }
}

val nullScript: Script?
	get() =
		null

fun Script?.plus(line: ScriptLine): Script =
	Script(this, line)

fun Script?.plus(word: Word, rhs: Script? = null): Script =
	plus(word lineTo rhs)

fun Appendable.append(script: Script): Appendable =
	this
		.ifNotNull(script.lhs) { append(it) }
		.append(script.term)
