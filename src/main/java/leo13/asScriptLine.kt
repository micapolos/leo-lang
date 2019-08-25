package leo13

import leo.base.*
import leo9.*
import leo9.Stack

// TODO: Convert to interface, and replace "super.toString()" with "asScriptLine.toString()" in implementations
abstract class AsScriptLine {
	override fun toString() = asScriptLine.toString()
	abstract val asScriptLine: ScriptLine
	val asScript: Script get() = asScriptLine.rhs
}

fun <V : AsScriptLine> V?.orNullAsScriptLine(name: String) =
	this?.asScriptLine ?: name lineTo script(nullScriptLine)

val <V : AsScriptLine> V?.orNullAsScript: Script
	get() =
		if (this == null) script()
		else asScriptLine.script

val <V : AsScriptLine> Stack<V>.asScript: Script
	get() = asScript { asScriptLine }

val <V : AsScriptLine> Stack<V>.asNoNullScript: Script
	get() = map { asScriptLine }.script

fun <F : AsScriptLine, R : AsScriptLine> asMetaFirstScript(firstName: String, firstOrNull: F?, remaining: Seq<R>): Script =
	firstOrNull
		?.asScriptLine
		.let { firstLineOrNull ->
			remaining.map { asScriptLine }.let { remainingLines ->
				if (firstLineOrNull != null) firstLineOrNull.script.fold(remainingLines) { plus(it) }
				else script().fold(remainingLines.mapFirst { meta(firstName) }) { plus(it) }
			}
		}

fun <V : AsScriptLine> Stack<V>.asScriptLine(name: String): ScriptLine =
	name lineTo asScript

fun <V : AsScriptLine> Stack<V>.asSeparatedScript(name: String): Script =
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
