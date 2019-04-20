package leo32.runtime

import leo.base.*
import leo.binary.zero
import leo32.base.at
import leo32.base.i32
import leo32.base.isEmpty
import leo32.base.size

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
	.append(code.line, indent)

fun Appendable.append(line: Line, indent: Indent): Appendable =
	null
		?: maybeAppendString(line)
		?: appendPrim(line, indent)


fun Appendable.appendPrim(line: Line, indent: Indent): Appendable = this
	.append(line.name)
	.run {
		when (line.value.lineList.size.int) {
			0 -> this
			1 -> this
				.append(' ')
				.append(line.value.lineList.at(zero.i32).code, indent, false)
			else -> indented(indent) { childIndent -> this
				.append('\n')
				.append(line.value.code, childIndent)
			}
		}
	}

fun Appendable.maybeAppendString(line: Line) =
	ifOrNull(line.name == stringSymbol
		&& line.value.lineList.size.int == 1) {
		line.value.lineList.at(0.i32).let { stringLine ->
			ifOrNull(stringLine.value.lineList.isEmpty) {
				append("\"${stringLine.name}\"")
			}
		}
	}