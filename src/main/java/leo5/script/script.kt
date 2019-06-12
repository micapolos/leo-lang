package leo5.script

import leo.base.Empty
import leo.base.appendableString
import leo.base.empty
import leo.base.fold

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class ExtensionScript(val extension: Extension) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(extension: Extension): Script = ExtensionScript(extension)

fun Script.extend(line: Line): Script = ExtensionScript(Extension(this, line))
fun script(vararg lines: Line) = script(empty).fold(lines, Script::extend)
fun script(string: String) = script(string lineTo script(empty))

val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.extensionOrNull get() = (this as? ExtensionScript)?.extension

fun Appendable.append(script: Script): Appendable = when (script) {
	is EmptyScript -> this
	is ExtensionScript -> append(script.extension)
}

val Script.string get() = appendableString { it.append(this) }

tailrec fun Script.extendReversedLines(script: Script): Script = when (script) {
	is EmptyScript -> this
	is ExtensionScript -> extend(script.extension.line).extendReversedLines(script.extension.script)
}

val Script.reverseLines get() = script().extendReversedLines(this)

tailrec fun Script.wrap(string: String, count: Int, acc: Script = script()): Extension? =
	if (count == 0) extension(this, string lineTo acc.reverseLines)
 	else {
		val extensionOrNull = extensionOrNull
		extensionOrNull?.script?.wrap(string, count.dec(), acc.extend(extensionOrNull.line))
	}

tailrec fun Script.lineCount(acc: Int = 0): Int = when (this) {
	is EmptyScript -> acc
	is ExtensionScript -> extension.script.lineCount(acc.inc())
}

val Script.lineCount get() = lineCount()