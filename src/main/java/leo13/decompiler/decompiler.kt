package leo13.decompiler

import leo.base.fail
import leo.base.notNullOrError
import leo.binary.Bit
import leo13.ObjectScripting
import leo13.Processor
import leo13.decompilerName
import leo13.interpreter.ValueTyped
import leo13.script.*
import leo13.type.*
import leo13.value.scriptOrNull

data class Decompiler(val processor: Processor<Script>) : ObjectScripting(), Processor<ValueTyped> {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = decompilerName lineTo script(processor.scriptingLine)

	override fun process(typed: ValueTyped) =
		Decompiler(processor.process(typed.value.scriptOrNull!!))
}

fun Processor<Script>.decompiler() = Decompiler(this)

val Type.staticScriptOrNull: Script?
	get() =
		when (this) {
			is EmptyType -> script()
			is LinkType -> link.staticScriptOrNull
			is OptionsType -> null
			is ArrowType -> null
		}

val TypeLink.staticScriptOrNull: Script?
	get() =
		lhs.staticScriptOrNull?.let { lhsStaticScript ->
			item.staticScriptLineOrNull?.let { itemStaticScriptLine ->
				lhsStaticScript.plus(itemStaticScriptLine)
			}
		}

val TypeItem.staticScriptLineOrNull: ScriptLine?
	get() =
		when (this) {
			is LineTypeItem -> line.staticScriptLineOrNull
			is RecurseTypeItem -> null
		}

val TypeLine.staticScriptLineOrNull: ScriptLine?
	get() =
		unexpandedRhs.staticScriptOrNull?.let { name lineTo it }

fun Type.script(value: Any?) =
	staticScriptOrNull ?: nonStaticScript(value)

fun Type.nonStaticScript(value: Any?): Script =
	when (this) {
		is EmptyType -> fail()
		is LinkType -> link.nonStaticScript(value)
		is OptionsType -> leo13.script.script(options.scriptLine(value))
		is ArrowType -> error("nonStaticScript")
	}

fun TypeLink.nonStaticScript(value: Any?): Script =
	item.staticScriptLineOrNull?.let { lhs.script(value).plus(it) }
		?: lhs.staticScriptOrNull?.let { script(item.scriptLine(value)) }
		?: (value as? Pair<Any?, Any?>)
			.notNullOrError("nonStaticScript")
			.run { lhs.script(first).plus(item.scriptLine(second)) }

fun TypeItem.scriptLine(value: Any?): ScriptLine =
	when (this) {
		is LineTypeItem -> line.scriptLine(value)
		is RecurseTypeItem -> error("scriptLine")
	}

fun TypeLine.scriptLine(value: Any?): ScriptLine =
	nativeScriptLineOrNull(value) ?: nonNativeScriptLine(value)

fun TypeLine.nativeScriptLineOrNull(value: Any?): ScriptLine? =
	when (this) {
		booleanTypeLine -> (value as Boolean).scriptLine
		bitTypeLine -> (value as Bit).scriptLine
		byteTypeLine -> (value as Byte).scriptLine
		else -> null
	}

fun TypeLine.nonNativeScriptLine(value: Any?): ScriptLine =
	name lineTo rhs.script(value)

fun Options.scriptLine(value: Any?): ScriptLine =
	if (rhsIsStatic) scriptLine(value as Int, null)
	else (value as? Pair<Any?, Any?>)
		.notNullOrError("scriptLine")
		.let { scriptLine(it.first as Int, it.second) }

fun Options.scriptLine(index: Int, value: Any?): ScriptLine =
	when (this) {
		is EmptyOptions -> error("scriptLine")
		is LinkOptions -> link.scriptLine(index, value)
	}

fun OptionsLink.scriptLine(index: Int, value: Any?): ScriptLine =
	if (index == 0) item.scriptLine(value)
	else lhs.scriptLine(index.dec(), value)
