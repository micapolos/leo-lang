package leo13

import leo.base.*
import leo13.script.*

interface Scriptable {
	val scriptableName: String
	val scriptableBody: Script
	val scriptableLine: ScriptLine get() = scriptableName lineTo scriptableBody
}

fun <V : Scriptable> V?.orNullAsScriptLine(name: String) =
	this?.scriptableLine ?: name lineTo script(nullScriptLine)

val <V : Scriptable> V?.orNullAsScript: Script
	get() =
		if (this == null) script()
		else scriptableLine.script

val <V : Scriptable> Stack<V>.asScript: Script
	get() = asScript { scriptableLine }

val <V : Scriptable> Stack<V>.asNoNullScript: Script
	get() = map { scriptableLine }.script

fun <F : Scriptable, R : Scriptable> asMetaFirstScript(firstName: String, firstOrNull: F?, remaining: Seq<R>): Script =
	firstOrNull
		?.scriptableLine
		.let { firstLineOrNull ->
			remaining.map { scriptableLine }.let { remainingLines ->
				if (firstLineOrNull != null) firstLineOrNull.script.fold(remainingLines) { plus(it) }
				else script().fold(remainingLines.mapFirst { metaFor(firstName) }) { plus(it) }
			}
		}

fun <V : Scriptable> Stack<V>.asScriptLine(name: String): ScriptLine =
	name lineTo asScript

fun <V : Scriptable> Stack<V>.asSeparatedScript(name: String): Script =
	(false to script()).fold(reverse) {
		true to second.plus(if (!first) it.scriptableLine else name lineTo script(it.scriptableLine))
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
