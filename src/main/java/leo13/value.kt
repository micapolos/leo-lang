package leo13

import leo.base.*
import leo9.fold
import leo9.push
import leo9.reverse

sealed class Value : Scriptable() {
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

val ValueLink.onlyLineOrNull get() = notNullIf(lhs.emptyOrNull == null) { line }

fun value(fn: Fn, vararg lines: ValueLine): Value = value(fn).fold(lines) { plus(it) }
fun value(vararg lines: ValueLine): Value = value(empty).fold(lines) { plus(it) }
fun value(name: String): Value = value(valueLine(name))
fun Value.plus(line: ValueLine): Value = value(link(this, line))

val Value.lineOrNullSeq: Seq<ValueLine?>
	get() = Seq<ValueLine?> {
		when (this) {
			is EmptyValue -> null
			is LinkValue -> link.lineOrNullSeqNode
			is FnValue -> null then seq<ValueLine?>()
		}
	}

val Value.lineSeq: Seq<ValueLine> get() = lineOrNullSeq.noNulls

val Script.value: Value get() = value().fold(lineStack.reverse) { plus(it.valueLine) }
val Value.scriptOrNull: Script?
	get() = leo9.stack<ScriptLine>().orNull.orNullFold(lineOrNullSeq) {
		it?.scriptLineOrNull?.let { push(it) }
	}?.script

val Value.onlyLineOrNull get() = linkOrNull?.onlyLineOrNull

fun Value.firstLineOrNull(name: String): ValueLine? =
	lineSeq.firstOrNull { it.name == name }

fun Value.accessOrNull(name: String): Value? =
	onlyLineOrNull?.rhs?.firstLineOrNull(name)?.let { value(it) }