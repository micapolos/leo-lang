package leo12

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.notNullIf
import leo9.Stack
import leo9.push
import leo9.stack

sealed class Script

data class EmptyScript(val empty: Empty) : Script() {
	override fun toString() = ""
}

data class BodyScript(val body: ScriptBody) : Script() {
	override fun toString() = "$body"
}

sealed class ScriptBody

data class NameScriptBody(val name: String) : ScriptBody() {
	override fun toString() = "$name()"
}

data class LinkScriptBody(val link: ScriptLink) : ScriptBody() {
	override fun toString() = "$link"
}

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = "$lhs$line"
}

data class ScriptLine(val name: String, val rhs: ScriptBody) {
	override fun toString() = "$name($rhs)"
}

data class ScriptHeader(val name: String, val rhs: Script)

val Script.isEmpty get() = (this is EmptyScript)
val Script.bodyOrNull get() = (this as? BodyScript)?.body
val ScriptBody.linkOrNull get() = (this as? LinkScriptBody)?.link
val ScriptBody.onlyLineOrNull get() = linkOrNull?.onlyLineOrNull
fun ScriptBody.onlyScriptOrNull(name: String) =
	when (this) {
		is NameScriptBody -> script(empty)
		is LinkScriptBody -> link.onlyLineOrNull?.rhsOrNull(name)?.let { script(it) }
	}

val ScriptLink.onlyLineOrNull get() = notNullIf(lhs.isEmpty) { line }
fun ScriptLine.rhsOrNull(name: String) = notNullIf(name == this.name) { rhs }

fun Script.bodyStackOrNull(name: String): Stack<ScriptBody>? =
	when (this) {
		is EmptyScript -> stack()
		is BodyScript -> body.bodyStackOrNull(name)
	}

fun ScriptBody.bodyStackOrNull(name: String): Stack<ScriptBody>? =
	when (this) {
		is NameScriptBody -> null
		is LinkScriptBody -> link.bodyStackOrNull(name)
	}

fun ScriptLink.bodyStackOrNull(name: String): Stack<ScriptBody>? =
	lhs.bodyStackOrNull(name)?.let { stack ->
		line.bodyOrNull(name)?.let { body ->
			stack.push(body)
		}
	}

fun ScriptLine.bodyOrNull(name: String): ScriptBody? =
	notNullIf(this.name == name) { rhs }

val Script.lineStackOrNull: Stack<ScriptLine>?
	get() =
		when (this) {
			is EmptyScript -> stack()
			is BodyScript -> lineStackOrNull
		}

val ScriptBody.lineStackOrNull: Stack<ScriptLine>?
	get() =
		linkOrNull?.lineStackOrNull

val ScriptLink.lineStackOrNull
	get() =
		lhs.lineStackOrNull?.push(line)

val Script.headerOrNull get() = bodyOrNull?.headerOrNull
val ScriptBody.headerOrNull
	get() = when (this) {
		is NameScriptBody -> header(name, script(empty))
		is LinkScriptBody -> notNullIf(link.lhs.isEmpty) { header(link.line.name, script(link.line.rhs)) }
	}

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(body: ScriptBody): Script = BodyScript(body)
fun scriptBody(name: String): ScriptBody = NameScriptBody(name)
fun body(link: ScriptLink): ScriptBody = LinkScriptBody(link)
fun link(lhs: Script, line: ScriptLine) = ScriptLink(lhs, line)
infix fun String.lineTo(rhs: ScriptBody) = ScriptLine(this, rhs)
operator fun Script.plus(line: ScriptLine): ScriptBody =
	body(link(this, line))

operator fun ScriptBody.plus(line: ScriptLine): ScriptBody =
	body(link(script(this), line))

fun body(name: String, line: ScriptLine, vararg lines: ScriptLine) =
	scriptBody(name).plus(line).fold(lines) { plus(it) }

fun body(line: ScriptLine, vararg lines: ScriptLine) =
	body(link(script(empty), line)).fold(lines) { plus(it) }

fun header(name: String, script: Script) = ScriptHeader(name, script)