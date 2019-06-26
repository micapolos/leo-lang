package leo6

data class Cursor(val parent: Parent, val script: Script) {
	override fun toString() = "$parent$script"
}

infix fun Parent.cursorTo(script: Script) = Cursor(this, script)
fun cursor() = parent().cursorTo(script())
fun cursor(script: Script) = Cursor(parent(), script)

operator fun Cursor.plus(line: Line) = copy(script = script + line)

fun Cursor.begin(word: Word) = Cursor(parent(this linkTo word), script())
val Cursor.end get() = parent.linkOrNull?.let { it.cursor.plus(it.word lineTo script) }

operator fun Cursor.plus(token: Token) = when (token) {
	is BeginToken -> plus(token.begin)
	is EndToken -> plus(token.end)
}

operator fun Cursor.plus(begin: Begin) = begin(begin.word)
operator fun Cursor.plus(end: End) = this.end

fun Cursor.with(script: Script) = copy(script = script)

fun Cursor.begin(string: String) = begin(word(string))

fun Cursor.inScriptAt(word: Word): Cursor? =
	script
		.linkAt(word)
		?.let { parent.cursorTo(it.leadingScript).begin(it.lastLine.word).with(it.lastLine.script) }

fun Cursor.inParentAt(word: Word): Cursor? =
	parent.cursorAt(word)

fun Cursor.at(word: Word): Cursor? =
	inScriptAt(word) ?: inParentAt(word)

fun Cursor.inScriptAt(path: Path): Cursor? = when (path) {
	is NothingPath -> this
	is LinkPath ->
		null
			?: inScriptAt(path.link.firstWord)
				?.run { inScriptAt(path.link.remainingPath) }
			?: script.linkOrNull?.leadingScript?.let { with(it) }?.inScriptAt(path)
}

fun Cursor.inParentAt(path: Path): Cursor? =
	parent.cursorAt(path)

fun Cursor.at(path: Path): Cursor? =
	inScriptAt(path) ?: inParentAt(path)

fun Cursor.rootPath(acc: Path = path()): Path = when (parent) {
	is NothingParent -> acc
	is LinkParent -> parent.link.cursor.rootPath(parent.link.word pathTo acc)
}

fun Cursor.rootPathTo(path: Path): Path? =
	if (script.contains(path)) rootPath(path)
	else parent.rootPathTo(path)

fun Cursor.recursiveRootPathTo(path: Path, acc: Path = path()): Path? =
	if (acc.startsWith(path)) rootPath(path)
	else parent.recursiveRootPathTo(path, acc)