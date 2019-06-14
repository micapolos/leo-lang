package leo5.gen.kotlin

import leo.base.ifNotNull
import leo.base.runIf
import leo5.script.*

fun Appendable.appendType(script: Script): Appendable =
	ifNotNull(script.nonEmptyOrNull) { appendType(it) }

fun Appendable.appendType(scriptNonEmpty: ScriptNonEmpty): Appendable =
	scriptNonEmpty.script.emptyOrNull!!.run {
		appendType(scriptNonEmpty.line)
	}

fun Appendable.appendType(line: Line): Appendable =
	append(line.string.capitalize()).appendType(line.script)

fun Appendable.appendFields(script: Script, hasMore: Boolean = false): Appendable = when (script) {
	is EmptyScript -> this
	is NonEmptyScript -> appendFields(script.nonEmpty.script, true).appendField(script.nonEmpty.line).runIf(hasMore) { append(", ") }
}

fun Appendable.appendField(line: Line): Appendable =
	append("val ").append(line.string).append(": ").appendType(line)