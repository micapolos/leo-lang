package leo5.value

data class Cursor(val parent: Parent, val script: Script)
infix fun Parent.cursorTo(script: Script) = Cursor(this, script)
operator fun Cursor.plus(line: Line) = copy(script = script + line)
fun Cursor.begin(word: Word) = Cursor(parent(this linkTo word), script())
val Cursor.end get() = parent.linkOrNull?.let { it.cursor.plus(it.word lineTo script) }
