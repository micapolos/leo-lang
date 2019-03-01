package leo.script

import leo.Word
import leo.append
import leo.base.*
import leo.beginChar
import leo.endChar

data class Script(
	val wordTree: Tree<Word>) {
	override fun toString() = appendableString { it.append(this) }
}

val Tree<Word>.script: Script
	get() =
		Script(this)

val script: Script?
	get() =
		null

fun script(word: Word, rhs: Script? = null): Script =
	tree(word, rhs?.wordTree).script

fun Script?.plus(word: Word, rhs: Script? = null): Script =
	this?.wordTree.plusSibling(word, rhs?.wordTree).script

val Script.lhs: Script?
	get() =
		wordTree.previousSiblingOrNull?.script

val Script.rhs: Script?
	get() =
		wordTree.lastChildOrNull?.script

// === code ===

fun Appendable.append(script: Script): Appendable =
	this
		.ifNotNull(script.lhs, Appendable::append)
		.append(script.wordTree.value)
		.append(beginChar)
		.ifNotNull(script.rhs, Appendable::append)
		.append(endChar)
