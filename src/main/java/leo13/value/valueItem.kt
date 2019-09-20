package leo13.value

import leo13.ObjectScripting
import leo13.functionName
import leo13.script.ScriptLine
import leo13.script.metaFor

sealed class ValueItem : ObjectScripting()

data class FunctionValueItem(val function: ValueFunction) : ValueItem() {
	override fun toString() = super.toString()
	override val scriptingLine get() = function.scriptLine
}

data class LineValueItem(val line: ValueLine) : ValueItem() {
	override fun toString() = super.toString()
	override val scriptingLine = line.scriptLine
}

fun item(function: ValueFunction): ValueItem = FunctionValueItem(function)
fun item(line: ValueLine): ValueItem = LineValueItem(line)

val ValueItem.functionOrNull get() = (this as? FunctionValueItem)?.function
val ValueItem.lineOrNull get() = (this as? LineValueItem)?.line

fun ValueItem.rhsOrNull(selectedName: String): Value? =
	lineOrNull?.rhsOrNull(selectedName)

fun ValueItem.replaceOrNull(line: ValueLine): ValueItem? =
	lineOrNull?.replaceOrNull(line)?.let { item(it) }

val ValueItem.bodyScriptLine get() =
	when (this) {
		is FunctionValueItem -> function.scriptLine
		is LineValueItem -> line.bodyScriptLine.metaFor(functionName)
	}

val ValueItem.scriptLineOrNull get() =
	when (this) {
		is FunctionValueItem -> function.scriptLine
		is LineValueItem -> line.scriptLineOrNull
	}

val ScriptLine.valueItem: ValueItem
	get() =
		item(valueLine)
