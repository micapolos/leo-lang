package leo13

import leo.base.ifOrNull
import leo.base.orNull
import leo9.*

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

fun <V : Scriptable> Stack<V>.asSeparatedScript(name: String): Script =
	(false to script()).fold(reverse) {
		true to second.plus(if (!first) it.asScriptLine else name lineTo script(it.asScriptLine))
	}.second

fun <V : Any> Script.asStackOrNull(fn: ScriptLine.() -> V?): Stack<V>? =
	ifOrNull(!isEmpty) {
		if (this == nullScript) stack()
		else lineStack.mapOrNull { fn() }
	}

fun <V : Any> ScriptLine.asStackOrNull(name: String, fn: ScriptLine.() -> V?): Stack<V>? =
	ifOrNull(this.name == name) { rhs.asStackOrNull(fn) }

fun <V : Any> Script.asSeparatedStackOrNull(name: String, fn: ScriptLine.() -> V?): Stack<V>? =
	stack<V>().orNull.fold(lineStack.reverse) { line ->
		this?.run {
			if (this.isEmpty) line.fn()?.let { push(it) }
			else ifOrNull(line.name == name) {
				line.rhs.onlyLineOrNull?.let { line ->
					line.fn()?.let { push(it) }
				}
			}
		}
	}
