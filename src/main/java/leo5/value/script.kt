package leo5.value

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script

data class EmptyScript(val empty: Empty): Script()
data class LinkScript(val link: ScriptLink): Script()

fun script(): Script = EmptyScript(empty)
operator fun Script.plus(line: Line): Script = LinkScript(this linkTo line)
fun script(vararg lines: Line) = script().fold(lines) { plus(it) }

tailrec fun Script.cut(word: Word): Script = when (this) {
	is EmptyScript -> this
	is LinkScript -> if (link.line.word == word) this else link.script.cut(word)
}
