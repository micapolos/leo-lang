package leo32.runtime

import leo.base.*
import leo.binary.zero
import leo32.base.at
import leo32.base.i32

data class ScriptCode(val script: Script) {
	override fun toString() = appendableString { it.append(this) }
}

data class LineCode(val line: Line) {
	override fun toString() = appendableString { it.append(this) }
}

val Script.code get() =
	ScriptCode(this)

val Line.code get() =
	LineCode(this)

fun Appendable.append(code: ScriptCode): Appendable =
	append(code, 0.indent)

fun Appendable.append(code: LineCode): Appendable =
	append(code, 0.indent, true)

fun Appendable.append(code: ScriptCode, indent: Indent): Appendable =
	fold(code.script.lineSeq.nullIntercept) {
		if (it == null) append('\n')
		else append(it.code, indent, true)
	}

fun Appendable.append(code: LineCode, indent: Indent, applyIndent: Boolean): Appendable = this
	.runIf(applyIndent) { append(indent) }
	.append(code.line.name)
	.run {
		when (code.line.value.lineList.size.int) {
			0 -> this
			1 -> this
				.append(' ')
				.append(code.line.value.lineList.at(zero.i32).code, indent, false)
			else -> indented(indent) { childIndent -> this
				.append('\n')
				.append(code.line.value.code, childIndent)
			}
		}
	}