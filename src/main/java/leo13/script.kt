package leo13

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class LinkScript(val link: ScriptLink) : Script()
data class ScriptLink(val lhs: Script, val line: ScriptLine)
data class ScriptLine(val name: String, val rhs: Script)

val Script.isEmpty get() = (this is EmptyScript)
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