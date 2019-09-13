package leo13.untyped.value

import leo.base.fold
import leo13.script.Script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.valueName
import leo9.*

data class Value(val itemStack: Stack<ValueItem>) {
	override fun toString() = scriptLine.toString()
}

val Stack<ValueItem>.value get() = Value(this)

fun value(vararg items: ValueItem) =
	stack(*items).value

fun value(line: ValueLine, vararg lines: ValueLine) =
	stack(line, *lines).map { item(this) }.value

fun value(name: String) = value(name lineTo value())

val Value.linkOrNull get() =
	itemStack.linkOrNull?.run { stack.value linkTo value }

val Value.firstItemOrNull: ValueItem?
	get() =
		linkOrNull?.rhsItem

fun Value.plus(vararg items: ValueItem) =
	itemStack.pushAll(*items).value

fun Value.plus(value: Value): Value =
	itemStack.pushAll(value.itemStack).value

fun Value.plus(line: ValueLine, vararg lines: ValueLine) =
	plus(item(line)).fold(lines) { plus(item(it)) }

fun Value.firstLineRhsOrNull(name: String): Value? =
	itemStack.mapFirst { rhsOrNull(name) }

fun Value.replaceLineOrNull(line: ValueLine): Value? =
	itemStack.updateFirst { replaceOrNull(line) }?.value

fun Value.getOrNull(name: String): Value? =
	firstItemOrNull
		?.lineOrNull
		?.rhs
		?.firstLineRhsOrNull(name)
		?.let { value(name lineTo it) }

fun Value.setOrNull(newLine: ValueLine): Value? =
	linkOrNull?.run {
		rhsItem.lineOrNull?.let { line ->
			line
				.rhs
				.replaceLineOrNull(newLine)?.let { rhs ->
					lhsValue.plus(item(line.name lineTo rhs))
				}
		}
	}

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
