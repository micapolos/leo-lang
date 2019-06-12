package leo5.gen.kotlin

import leo.base.ifNotNull
import leo.base.runIf
import leo5.script.*

fun Appendable.appendType(script: Script): Appendable =
	ifNotNull(script.extensionOrNull) { appendType(it) }

fun Appendable.appendType(extension: Extension): Appendable =
	extension.script.emptyOrNull!!.run {
		appendType(extension.line)
	}

fun Appendable.appendType(line: Line): Appendable =
	append(line.string.capitalize()).appendType(line.script)

fun Appendable.appendFields(script: Script, hasMore: Boolean = false): Appendable = when (script) {
	is EmptyScript -> this
	is ExtensionScript -> appendFields(script.extension.script, true).appendField(script.extension.line).runIf(hasMore) { append(", ") }
}

fun Appendable.appendField(line: Line): Appendable =
	append("val ").append(line.string).append(": ").appendType(line)