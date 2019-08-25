package leo13

import leo.base.ifOrNull
import leo9.Stack
import leo9.mapOrNull
import leo9.stack

abstract class Scriptable {
	override fun toString() = asScriptLine.toString()
	abstract val asScriptLine: ScriptLine
}

fun <V : Scriptable> V?.orNullAsScriptLine(name: String) =
	this?.asScriptLine ?: name lineTo script(nullScriptLine)

val <V : Scriptable> Stack<V>.asScript: Script
	get() = asScript { asScriptLine }

fun <V : Scriptable> Stack<V>.asScriptLine(name: String): ScriptLine =
	name lineTo asScript

fun <V : Any> Script.asStackOrNull(fn: ScriptLine.() -> V?): Stack<V>? =
	ifOrNull(!isEmpty) {
		if (this == nullScript) stack()
		else lineStack.mapOrNull { fn() }
	}

fun <V : Any> ScriptLine.asStackOrNull(name: String, fn: ScriptLine.() -> V?): Stack<V>? =
	ifOrNull(this.name == name) { rhs.asStackOrNull(fn) }
