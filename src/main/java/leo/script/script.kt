package leo.script

import leo.Word
import leo.base.appendableString
import leo.base.ifNotNull

data class Script(
	val lhs: Script?,
	val term: Term) {
	override fun toString() = appendableString { it.append(this) }
}

val nullScript: Script?
	get() =
		null

fun Script?.plus(term: Term): Script =
	Script(this, term)

fun Script?.plus(int: Int): Script =
	plus(IntTerm(int))

fun Script?.plus(float: Float): Script =
	plus(FloatTerm(float))

fun Script?.plus(string: String): Script =
	plus(StringTerm(string))

fun Script?.plus(word: Word, rhs: Script? = null): Script =
	plus(ScriptTerm(word, rhs))

fun Appendable.append(script: Script): Appendable =
	this
		.ifNotNull(script.lhs) { append(it).append(", ") }
		.append(script.term)

// === bit sequence
