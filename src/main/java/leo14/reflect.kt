package leo14

import leo13.Stack
import leo13.fold
import leo13.isEmpty
import leo13.reverse

fun <T> Stack<T>.reflectOrEmptyScript(reflectScriptLineFn: T.() -> ScriptLine): Script =
	if (isEmpty) script("empty")
	else script().fold(reverse) { plus(it.reflectScriptLineFn()) }

fun <T> Stack<T>.reflectOrEmptyScriptLine(name: String, reflectScriptLineFn: T.() -> ScriptLine): ScriptLine =
	name lineTo reflectOrEmptyScript(reflectScriptLineFn)