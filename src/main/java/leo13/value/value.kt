package leo13.value

import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.script.Script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script

data class Value(val itemStack: Stack<ValueItem>) {
	override fun toString() = scriptLine.toString()
}

val Stack<ValueItem>.value get() = Value(this)

fun value(vararg items: ValueItem) =
	stack(*items).value

fun value(line: ValueLine, vararg lines: ValueLine) =
	stack(line, *lines).map { item(this) }.value

fun value(name: String) = value(name lineTo value())

val Value.linkOrNull: ValueLink?
	get() =
	itemStack.linkOrNull?.run { stack.value linkTo value }

val Value.firstItemOrNull: ValueItem?
	get() =
		linkOrNull?.rhsItem

val Value.onlyItemOrNull: ValueItem?
	get() =
		linkOrNull?.run {
			notNullIf(lhsValue.isEmpty) {
				rhsItem
			}
		}

val Value.onlyLineOrNull
	get() =
		linkOrNull?.let { link ->
			ifOrNull(link.lhsValue.isEmpty) {
				link.rhsItem.lineOrNull
			}
		}

fun Value.plus(vararg items: ValueItem) =
	itemStack.pushAll(*items).value

fun Value.plus(value: Value): Value =
	itemStack.pushAll(value.itemStack).value

fun Value.plus(line: ValueLine, vararg lines: ValueLine) =
	plus(item(line)).fold(lines) { plus(item(it)) }

fun Value.firstItemOrNull(name: String): ValueItem? =
	itemStack.mapFirst { itemOrNull(name) }

fun Value.replaceLineOrNull(line: ValueLine): Value? =
	itemStack.updateFirst { replaceOrNull(line) }?.value

fun Value.getOrNull(name: String): Value? =
	updateLineRhsOrNull {
		firstItemOrNull(name)?.run {
			value(this)
		}
	}

fun Value.setOrNull(newLine: ValueLine): Value? =
	linkOrNull?.run {
		rhsItem.lineOrNull?.let { line ->
			line
				.rhs
				.replaceLineOrNull(newLine)
				?.let { rhs -> lhsValue.plus(item(line.name lineTo rhs)) }
		}
	}

val Value.contentOrNull
	get() =
		firstItemOrNull?.lineOrNull?.rhs

val Value.functionOrNull
	get() =
		firstItemOrNull?.functionOrNull

val Value.isEmpty get() = itemStack.isEmpty

val Value.scriptLine get() =
	valueName lineTo bodyScript.emptyIfEmpty

val Value.bodyScript get() =
	itemStack.map { bodyScriptLine }.script

val Value.previousOrNull: Value?
	get() =
		itemStack.linkOrNull?.stack?.value

val Value.everythingOrNull: Value?
	get() =
		itemStack.linkOrNull?.value?.lineOrNull?.rhs

val Value.scriptOrNull: Script?
	get() =
		itemStack.mapOrNull { scriptLineOrNull }?.script

val Script.value: Value
	get() =
		lineStack.map { valueItem }.value

fun Value.updateLineRhsOrNull(fn: Value.() -> Value?): Value? =
	linkOrNull?.run {
		rhsItem.lineOrNull?.rhs?.fn()
	}

fun Value.apply(value: Value): Value =
	contentOrNull!!.onlyItemOrNull!!.valueApply(value)