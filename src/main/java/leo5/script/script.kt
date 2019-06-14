package leo5.script

import leo.base.Empty
import leo.base.appendableString
import leo.base.empty
import leo.base.fold

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class NonEmptyScript(val nonEmpty: ScriptNonEmpty) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(scriptNonEmpty: ScriptNonEmpty): Script = NonEmptyScript(scriptNonEmpty)

fun Script.extend(line: Line): Script = NonEmptyScript(ScriptNonEmpty(this, line))
fun script(vararg lines: Line) = script(empty).fold(lines, Script::extend)
fun script(string: String) = script(string lineTo script(empty))

val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.nonEmptyOrNull get() = (this as? NonEmptyScript)?.nonEmpty

val Script.string get() = appendableString { it.append(this) }

tailrec fun Script.extendReversedLines(script: Script): Script = when (script) {
	is EmptyScript -> this
	is NonEmptyScript -> extend(script.nonEmpty.line).extendReversedLines(script.nonEmpty.script)
}

val Script.reverseLines get() = script().extendReversedLines(this)

tailrec fun Script.wrap(string: String, count: Int, acc: Script = script()): ScriptNonEmpty? =
	if (count == 0) nonEmpty(this, string lineTo acc.reverseLines)
 	else {
		val extensionOrNull = nonEmptyOrNull
		extensionOrNull?.script?.wrap(string, count.dec(), acc.extend(extensionOrNull.line))
	}

tailrec fun Script.lineCount(acc: Int = 0): Int = when (this) {
	is EmptyScript -> acc
	is NonEmptyScript -> nonEmpty.script.lineCount(acc.inc())
}

val Script.lineCount get() = lineCount()