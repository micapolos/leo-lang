package leo16

import leo13.caseName

data class Case(val selectedWord: String, val fn: Value.() -> Value) {
	override fun toString() = sentence.toString()
}

fun String.gives(fn: Value.() -> Value) = Case(this, fn)

val Case.sentence get() = caseName(selectedWord(fn.toString()()))