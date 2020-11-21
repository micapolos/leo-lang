package leo21.token.type.compiler

import leo.base.ifOrNull
import leo13.Stack
import leo13.first
import leo13.mapFirst
import leo13.push
import leo13.stack
import leo14.ScriptLine
import leo14.Scriptable
import leo14.reflectOrEmptyScriptLine
import leo21.compiled.LineCompiled
import leo21.compiled.castOrNull
import leo21.type.Line
import leo21.type.fieldOrNull
import leo21.type.isEmpty
import leo21.type.matches
import leo21.type.scriptLine

data class Lines(val lineStack: Stack<Line>) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = lineStack.reflectOrEmptyScriptLine("lines") { scriptLine }
}

val Stack<Line>.lines get() = Lines(this)
val emptyLines = Lines(stack())
fun Lines.plus(line: Line) = lineStack.push(line).lines

fun Line.castOrNull(lineCompiled: LineCompiled): LineCompiled? =
	lineCompiled.castOrNull(this)?.t

fun Lines.cast(lineCompiled: LineCompiled): LineCompiled =
	lineStack.mapFirst { castOrNull(lineCompiled) } ?: lineCompiled

fun Lines.resolveOrNull(line: Line): Line? =
	line.fieldOrNull?.let { field ->
		ifOrNull(field.rhs.isEmpty) {
			lineStack.first { it.matches(field.name) }
		}
	}

fun Lines.resolve(line: Line): Line =
	resolveOrNull(line) ?: line
