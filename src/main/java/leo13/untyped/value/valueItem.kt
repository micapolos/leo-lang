package leo13.untyped.value

import leo13.script.metaFor
import leo13.untyped.functionName

sealed class ValueItem

data class FunctionValueItem(val function: ValueFunction): ValueItem()
data class LineValueItem(val line: ValueLine): ValueItem()

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
	lineOrNull?.scriptLine