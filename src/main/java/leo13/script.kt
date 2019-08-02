package leo13

import leo.base.*

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class LinkScript(val link: ScriptLink) : Script()
data class ScriptLink(val lhs: Script, val line: ScriptLine)
data class ScriptLine(val name: String, val rhs: Script)

val Script.isEmpty get() = (this is EmptyScript)
val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.linkOrNull get() = (this as? LinkScript)?.link

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(link: ScriptLink): Script = LinkScript(link)
fun link(lhs: Script, line: ScriptLine) = ScriptLink(lhs, line)
infix fun String.lineTo(rhs: Script) = ScriptLine(this, rhs)
fun Script.plus(line: ScriptLine) = script(link(this, line))
fun script(vararg lines: ScriptLine) = script(empty).fold(lines) { plus(it) }

val Script.code: String get() = linkOrNull?.code ?: ""
val ScriptLink.code get() = "$lhs$line"
val ScriptLine.code get() = "$name($rhs)"

// --- access int

data class ScriptAccess(val line: ScriptLine, val int: Int)

val Script.accessOrNull
	get() =
		linkOrNull?.accessOrNull

val ScriptLink.accessOrNull
	get() =
		ifOrNull(lhs.isEmpty) {
			line.accessOrNull
		}

val ScriptLine.accessOrNull
	get() =
		rhs.accessOrNull(name, 0)

fun Script.accessOrNull(name: String, int: Int): ScriptAccess? =
	linkOrNull?.accessOrNull(name, int)

fun ScriptLink.accessOrNull(name: String, int: Int) =
	line.accessOrNull(name, int) ?: lhs.accessOrNull(name, int.inc())

fun ScriptLine.accessOrNull(name: String, int: Int) =
	notNullIf(name == this.name) {
		ScriptAccess(this, int)
	}
