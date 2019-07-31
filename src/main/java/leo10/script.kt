package leo10

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.notNullIf

sealed class Script

data class EmptyScript(
	val empty: Empty) : Script()

data class LinkScript(
	val link: ScriptLink) : Script()

data class FunctionScript(
	val function: Function) : Script()

data class ScriptLink(
	val script: Script,
	val line: ScriptLine)

data class ScriptLine(
	val name: String,
	val script: Script)

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(link: ScriptLink): Script = LinkScript(link)
fun script(function: Function): Script = FunctionScript(function)
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)
fun Script.plus(line: ScriptLine) = script(this linkTo line)
infix fun String.lineTo(script: Script) = ScriptLine(this, script)
fun script(vararg lines: ScriptLine): Script = script(empty).fold(lines) { plus(it) }
val Script.linkOrNull get() = (this as? LinkScript)?.link
val Script.link get() = linkOrNull!!
val Script.lhs get() = link.script
val Script.name get() = link.line.name
val Script.rhs get() = link.line.script
fun Script.at(name: String) = atOrNull(name)!!
fun Script.atOrNull(name: String): Script? = link.line.atOrNull(name) ?: link.script.atOrNull(name)
fun ScriptLine.atOrNull(name: String) = notNullIf(this.name == name) { script }