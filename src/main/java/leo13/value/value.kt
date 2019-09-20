package leo13.value

import leo.base.*
import leo13.LeoObject
import leo13.fold
import leo13.push
import leo13.reverse
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Value : LeoObject() {
	override val scriptableName get() = "value"
	override val scriptableBody get() = script(scriptableValueName lineTo scriptableValueBody)

	abstract val scriptableValueName: String
	abstract val scriptableValueBody: Script
}

data class EmptyValue(val empty: Empty) : Value() {
	override fun toString() = super.toString()
	override val scriptableValueName = "empty"
	override val scriptableValueBody get() = script()
}

data class LinkValue(val link: ValueLink) : Value() {
	override fun toString() = super.toString()
	override val scriptableValueName get() = link.scriptableName
	override val scriptableValueBody get() = link.scriptableBody
}

data class FnValue(val fn: Fn) : Value() {
	override fun toString() = super.toString()
	override val scriptableValueName get() = fn.scriptableName
	override val scriptableValueBody get() = fn.scriptableBody
}

fun value(empty: Empty): Value = EmptyValue(empty)
fun value(link: ValueLink): Value = LinkValue(link)
fun value(fn: Fn): Value = FnValue(fn)

val Value.emptyOrNull get() = (this as? EmptyValue)?.empty
val Value.linkOrNull get() = (this as? LinkValue)?.link
val Value.fnOrNull get() = (this as? FnValue)?.fn

val Value.isEmpty get() = emptyOrNull != null

val ValueLink.onlyLineOrNull get() = notNullIf(lhs.emptyOrNull != null) { line }

fun value(fn: Fn, vararg lines: ValueLine): Value = value(fn).fold(lines) { plus(it) }
fun value(vararg lines: ValueLine): Value = value(empty).fold(lines) { plus(it) }
fun value(name: String): Value = value(valueLine(name))
fun Value.plus(line: ValueLine): Value = value(link(this, line))

val Value.lineOrNullSeq: Seq<ValueLine?>
	get() = Seq {
		when (this) {
			is EmptyValue -> null
			is LinkValue -> link.lineOrNullSeqNode
			is FnValue -> null then seq<ValueLine?>()
		}
	}

val Value.lineSeq: Seq<ValueLine> get() = lineOrNullSeq.noNulls

val Script.value: Value get() = value().fold(lineStack.reverse) { plus(it.valueLine) }
val Value.scriptOrNull: Script?
	get() = leo13.stack<ScriptLine>().orNull.orNullFold(lineOrNullSeq) {
		it?.scriptLineOrNull?.let { push(it) }
	}?.reverse?.script

val Value.onlyLineOrNull get() = linkOrNull?.onlyLineOrNull

fun Value.firstLineOrNull(name: String): ValueLine? =
	lineSeq.firstOrNull { it.name == name }

fun Value.replaceOrNull(line: ValueLine): Value? =
	linkOrNull?.let { link ->
		if (link.line.name == line.name) value(link(link.lhs, line))
		else link.lhs.replaceOrNull(line)?.let { replacedLhs ->
			value(link(replacedLhs, link.line))
		}
	}

fun Value.getOrNull(name: String): Value? =
	onlyLineOrNull?.rhs?.firstLineOrNull(name)?.let { value(it) }

fun Value.setOrNull(line: ValueLine): Value? =
	onlyLineOrNull?.let { onlyLine ->
		onlyLine.rhs.replaceOrNull(line)?.let { replaced ->
			value(onlyLine.name lineTo replaced)
		}
	}

val Value.wrapOrNull: Value?
	get() =
		linkOrNull?.run {
			notNullIf(line.rhs.isEmpty) {
				value(line.name lineTo lhs)
			}
		}