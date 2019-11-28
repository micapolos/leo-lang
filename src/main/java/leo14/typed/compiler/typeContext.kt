package leo14.typed.compiler

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo13.stack
import leo14.typed.Line
import leo14.typed.LineNative
import leo14.typed.line

data class TypeContext(
	val lineNativeStack: Stack<LineNative>)

val Stack<LineNative>.typeContext get() = TypeContext(this)
fun typeContext(vararg lineNatives: LineNative) = stack(*lineNatives).typeContext

fun TypeContext.resolve(line: Line): Line =
	lineNativeStack.mapFirst {
		notNullIf(line == line(name)) {
			line(this)
		}
	} ?: line