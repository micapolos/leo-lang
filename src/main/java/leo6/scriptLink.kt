package leo6

data class ScriptLink(val leadingScript: Script, val lastLine: Line) {
	override fun toString() = "$leadingScript$lastLine"
}

infix fun Script.linkTo(line: Line) = ScriptLink(this, line)

val ScriptLink.onlyLineOrNull get() = leadingScript.nothingOrNull?.run { lastLine }

val ScriptLink.pathOrNull get() = leadingScript.nothingOrNull?.run { lastLine.pathOrNull }

fun ScriptLink.at(word: Word) =
	if (lastLine.word == word) this
	else leadingScript.linkAt(word)

fun ScriptLink.contains(path: PathLink): Boolean =
	lastLine.contains(path) || leadingScript.contains(path)
