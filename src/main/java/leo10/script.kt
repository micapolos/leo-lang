package leo10

import leo.base.*

sealed class Script

data class EmptyScript(
	val empty: Empty) : Script() {
	override fun toString() = ""
}

data class LinkScript(
	val link: ScriptLink) : Script() {
	override fun toString() = "$link"
}

data class FunctionScript(
	val function: Function) : Script() {
	override fun toString() = "*"
}

data class ScriptLink(
	val script: Script,
	val line: ScriptLine) {
	override fun toString() = "$script$line"
}

data class ScriptLine(
	val name: String,
	val script: Script) {
	override fun toString() = "$name($script)"
}

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(link: ScriptLink): Script = LinkScript(link)
fun script(function: Function): Script = FunctionScript(function)
infix fun Script.linkTo(line: ScriptLine) = ScriptLink(this, line)
fun link(script: Script, line: ScriptLine) = ScriptLink(script, line)
fun Script.plus(line: ScriptLine) = script(this linkTo line)
infix fun String.lineTo(script: Script) = ScriptLine(this, script)
fun script(vararg lines: ScriptLine): Script = script(empty).fold(lines) { plus(it) }
fun script(function: Function, vararg lines: ScriptLine): Script = script(function).fold(lines) { plus(it) }
val Script.linkOrNull get() = (this as? LinkScript)?.link
val Script.link get() = linkOrNull!!
val Script.lhs get() = link.script
val Script.name get() = link.line.name
val Script.rhs get() = link.line.script
fun Script.at(name: String) = atOrNull(name)!!
fun Script.atOrNull(name: String): Script? = linkOrNull?.line?.atOrNull(name) ?: linkOrNull?.script?.atOrNull(name)
fun ScriptLine.atOrNull(name: String) = notNullIf(this.name == name) { script }

val Script.isEmpty get() = this is EmptyScript
val ScriptLink.onlyLineOrNull get() = failIfOr(!script.isEmpty) { line }
val Script.onlyLineOrNull get() = linkOrNull?.onlyLineOrNull

// TODO(micapolos): Return null on duplicate fields!!!
operator fun Script.get(name: String) = onlyLineOrNull?.let { line ->
	line.script.atOrNull(name)?.let { script ->
		script(name lineTo script)
	}
}


