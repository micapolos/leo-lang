package leo11

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.ifOrNull
import leo9.Stack
import leo9.push
import leo9.stack

sealed class Script

data class EmptyScript(val empty: Empty) : Script() {
	override fun toString() = code
}

data class LinkScript(val link: ScriptLink) : Script() {
	override fun toString() = code
}

data class ScriptLink(val name: String, val args: ScriptArgs) {
	override fun toString() = code
}

sealed class ScriptArgs
data class EmptyScriptArgs(val empty: Empty) : ScriptArgs()
data class LinkScriptArgs(val link: ScriptLink) : ScriptArgs()
data class PairScriptArgs(val pair: ScriptLinkPair) : ScriptArgs()

data class ScriptLinkPair(val lhs: ScriptLink, val rhs: ScriptLink)

data class ScriptLine(val name: String, val script: Script)
data class ScriptLinkLine(val name: String, val link: ScriptLink)

val Script.isEmpty get() = this is EmptyScript
val Script.linkOrNull get() = (this as? LinkScript)?.link
val ScriptArgs.linkOrNull get() = (this as? LinkScriptArgs)?.link
val Script.lineOrNull get() = linkOrNull?.lineOrNull
val ScriptLink.lineOrNull
	get() =
		when (args) {
			is EmptyScriptArgs -> name lineTo script(empty)
			is LinkScriptArgs -> name lineTo script(args.link)
			is PairScriptArgs -> null
		}

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(link: ScriptLink): Script = LinkScript(link)

fun link(string: String, args: ScriptArgs) = ScriptLink(string, args)

fun scriptArgs(empty: Empty): ScriptArgs = EmptyScriptArgs(empty)
fun args(link: ScriptLink): ScriptArgs = LinkScriptArgs(link)
fun args(pair: ScriptLinkPair): ScriptArgs = PairScriptArgs(pair)

fun pair(lhs: ScriptLink, rhs: ScriptLink) = ScriptLinkPair(lhs, rhs)
infix fun String.lineTo(script: Script) = ScriptLine(this, script)
infix fun String.lineTo(link: ScriptLink) = ScriptLinkLine(this, link)

fun Script.plus(line: ScriptLine): Script =
	script(plusLink(line))

fun Script.plusLink(line: ScriptLine): ScriptLink =
	when (this) {
		is EmptyScript ->
			when (line.script) {
				is EmptyScript -> link(line.name, scriptArgs(empty))
				is LinkScript -> link(line.name, args(line.script.link))
			}
		is LinkScript ->
			when (line.script) {
				is EmptyScript -> link(line.name, args(link))
				is LinkScript -> link(line.name, args(pair(link, line.script.link)))
			}
	}

val Script.lineStack: Stack<ScriptLine>
	get() =
		when (this) {
			is EmptyScript -> stack()
			is LinkScript -> link.lineStack
		}

val ScriptLink.lineStack
	get() =
		args.rhs.lineStack.push(name lineTo args.lhs)

fun script(vararg lines: ScriptLine): Script = script(empty).fold(lines) { plus(it) }

val Script.code: String
	get() =
		when (this) {
			is EmptyScript -> empty.code
			is LinkScript -> link.code
		}

val Empty.code get() = ""

val ScriptLink.code: String get() = args.code(name.code)

val String.code get() = this // TODO: Escape what's needed

fun ScriptArgs.code(nameCode: String): String =
	when (this) {
		is EmptyScriptArgs -> "$nameCode()"
		is LinkScriptArgs -> "$nameCode(${link.code})"
		is PairScriptArgs -> "${pair.lhs.code}$nameCode(${pair.rhs.code})"
	}

val ScriptArgs.lhs
	get() =
		when (this) {
			is EmptyScriptArgs -> script(empty)
			is LinkScriptArgs -> script(empty)
			is PairScriptArgs -> script(pair.lhs)
		}

val ScriptArgs.rhs
	get() =
		when (this) {
			is EmptyScriptArgs -> script(empty)
			is LinkScriptArgs -> script(link)
			is PairScriptArgs -> script(pair.rhs)
		}

fun Script.linkStackOrNull(name: String): Stack<ScriptLink>? =
	when (this) {
		is EmptyScript -> stack()
		is LinkScript -> link.linkStackOrNull(name)
	}

fun ScriptLink.linkStackOrNull(name: String): Stack<ScriptLink>? =
	ifOrNull(name == this.name) {
		when (args) {
			is EmptyScriptArgs -> null
			is LinkScriptArgs -> stack(args.link)
			is PairScriptArgs -> args.pair.lhs.linkStackOrNull(name)?.push(args.pair.rhs)
		}
	}

val ScriptLink.applyGet: ScriptLink?
	get() =
		TODO()