package leo6

import leo.base.fold

sealed class Script

data class NothingScript(val nothing: Nothing) : Script() {
	override fun toString() = ""
}

data class LinkScript(val link: ScriptLink) : Script() {
	override fun toString() = "$link"
}

fun script(): Script = NothingScript(nothing)
fun script(link: ScriptLink): Script = LinkScript(link)
operator fun Script.plus(line: Line) = script(this linkTo line)
fun script(vararg lines: Line) = script().fold(lines) { plus(it) }
fun script(string: String) = script(string lineTo script())

val Script.nothingOrNull get() = (this as? NothingScript)?.nothing
val Script.linkOrNull get() = (this as? LinkScript)?.link

fun Script.linkAt(word: Word): ScriptLink? = when (this) {
	is NothingScript -> null
	is LinkScript -> link.at(word)
}

val Script.pathOrNull: Path?
	get() = when (this) {
		is NothingScript -> path()
		is LinkScript -> link.pathOrNull
	}

fun Script.contains(path: Path): Boolean = when (path) {
	is NothingPath -> true
	is LinkPath -> contains(path.link)
}

fun Script.contains(pathLink: PathLink) = when (this) {
	is NothingScript -> false
	is LinkScript -> link.contains(pathLink)
}
