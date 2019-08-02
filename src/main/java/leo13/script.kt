package leo13

import leo.base.appendableString
import leo.base.fold
import leo.base.notNullIf
import leo9.*

data class Script(val lineStack: Stack<ScriptLine>)
data class ScriptLine(val name: String, val rhs: Script)

val Script.isEmpty get() = lineStack.isEmpty

fun script(lineStack: Stack<ScriptLine>) = Script(lineStack)
fun Script.plus(line: ScriptLine) = Script(lineStack.push(line))
fun script(vararg lines: ScriptLine) = Script(stack()).fold(lines) { plus(it) }
infix fun String.lineTo(rhs: Script) = ScriptLine(this, rhs)
val Script.onlyLineOrNull get() = lineStack.onlyOrNull

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

val Script.accessOrNull
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
