package leo13.value

import leo.base.notNullIf
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

data class NativeValueItem(val native: Any?, val lineFn: () -> ValueLine) : ValueItem() {
	override fun toString() = super.toString()
	override val scriptingLine get() = line.bodyScriptLine
	val line get() = lineFn()
	val unwrap get() = item(line)
}

fun item(function: ValueFunction): ValueItem = FunctionValueItem(function)
fun item(line: ValueLine): ValueItem = LineValueItem(line)
fun nativeItem(native: Any?, lineFn: () -> ValueLine): ValueItem = NativeValueItem(native, lineFn)

val ValueItem.functionOrNull get() = (this as? FunctionValueItem)?.function
val ValueItem.lineOrNull get() = (this as? LineValueItem)?.line

fun ValueItem.itemOrNull(selectedName: String): ValueItem? =
	when (this) {
		is LineValueItem -> line.rhsOrNull(selectedName)?.let { item(selectedName lineTo it) }
		is FunctionValueItem -> notNullIf(selectedName == functionName) { this }
		is NativeValueItem -> unwrap.itemOrNull(selectedName)
	}

fun ValueItem.replaceOrNull(line: ValueLine): ValueItem? =
	lineOrNull?.replaceOrNull(line)?.let { item(it) }

val ValueItem.bodyScriptLine: ScriptLine
	get() =
	when (this) {
		is FunctionValueItem -> function.scriptLine
		is LineValueItem -> line.bodyScriptLine.metaFor(functionName)
		is NativeValueItem -> unwrap.bodyScriptLine
	}

val ValueItem.scriptLineOrNull: ScriptLine?
	get() =
	when (this) {
		is FunctionValueItem -> function.scriptLine
		is LineValueItem -> line.scriptLineOrNull
		is NativeValueItem -> unwrap.scriptLineOrNull
	}

val ScriptLine.valueItem: ValueItem
	get() =
		item(valueLine)
