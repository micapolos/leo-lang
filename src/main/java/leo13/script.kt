package leo13

import leo.base.appendableString
import leo.base.notNullIf
import leo9.*

data class Script(val lineStack: Stack<ScriptLine>)
data class ScriptLine(val name: String, val rhs: Script)
data class ScriptLink(val lhs: Script, val line: ScriptLine)
data class ScriptLinkLine(val name: String, val rhs: ScriptLink)
data class ScriptArrow(val lhs: Script, val rhs: Script)

val Script.isEmpty get() = lineStack.isEmpty

val Stack<ScriptLine>.script get() = Script(this)
fun Script.plus(line: ScriptLine) = lineStack.push(line).script
fun script(vararg lines: ScriptLine) = stack(*lines).script
infix fun String.lineTo(rhs: Script) = ScriptLine(this, rhs)
infix fun String.lineTo(rhs: ScriptLink) = ScriptLinkLine(this, rhs)
fun link(lhs: Script, line: ScriptLine) = ScriptLink(lhs, line)
infix fun Script.arrowTo(rhs: Script) = ScriptArrow(this, rhs)
val Script.onlyLineOrNull get() = lineStack.onlyOrNull

val Script.linkOrNull
	get() =
		lineStack.linkOrNull?.let { link ->
			link(link.stack.script, link.value)
		}

val ScriptLink.script
	get() =
		lhs.lineStack.push(line).script

fun Script.arrowOrNull(name: String) =
	linkOrNull?.let { link ->
		notNullIf(link.line.name == name) {
			link.lhs arrowTo link.line.rhs
		}
	}

// --- code

val Script.code: String
	get() = appendableString { appendable ->
		appendable.fold(lineStack.reverse) { line ->
			appendable.append(line.code)
		}
	}

val ScriptLine.code get() = "$name(${rhs.code})"

// --- access int

data class ScriptAccess(val line: ScriptLine, val int: Int)

val Script.accessOrNull: ScriptAccess?
	get() =
		onlyLineOrNull?.accessOrNull

val ScriptLine.accessOrNull
	get() =
		rhs.accessOrNull(name, 0)

fun Script.accessOrNull(name: String, int: Int): ScriptAccess? =
	lineStack.mapFirst { accessOrNull(name, int) }

fun ScriptLine.accessOrNull(name: String, int: Int) =
	notNullIf(name == this.name) {
		ScriptAccess(this, int)
	}

fun Script.accessOrNull(name: String): Script? =
	onlyLineOrNull?.rhs?.lineStack?.mapOnly {
		notNullIf(name == this.name) {
			script(this)
		}
	}

// --- normalization

val Script.normalize: Script
	get() =
		script().fold(lineStack.reverse) { plusNormalized(it.normalize) }

val ScriptLine.normalize
	get() =
		name lineTo rhs.normalize

fun Script.plusNormalized(line: ScriptLine) =
	if (line.rhs.isEmpty) script(line.name lineTo this)
	else plus(line)

val Script.linkLineOrNull: ScriptLinkLine?
	get() =
		linkOrNull?.linkLineOrNull

val ScriptLink.linkLineOrNull: ScriptLinkLine?
	get() =
		if (lhs.isEmpty) line.rhs.linkOrNull?.let { line.name lineTo it }
		else if (line.rhs.isEmpty) lhs.linkOrNull?.let { line.name lineTo it }
		else null