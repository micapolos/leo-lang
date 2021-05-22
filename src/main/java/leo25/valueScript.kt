package leo25

import leo14.*

val Value.script: Script
	get() =
		when (this) {
			EmptyValue -> script()
			is LinkValue -> link.script
		}

val Link.script: Script
	get() =
		tail.script.plus(head.scriptLine)

val Line.scriptLine: ScriptLine
	get() =
		null
			?: literalScriptLineOrNull
			?: exactScriptLine

val Line.literalScriptLineOrNull: ScriptLine?
	get() =
		null
			?: textOrNull?.let { leo14.line(literal(it)) }
			?: numberOrNull?.let { leo14.line(literal(it)) }

val Line.exactScriptLine: ScriptLine
	get() =
		when (this) {
			is FieldLine -> line(field.scriptField)
			is FunctionLine -> function.scriptLine
			is NativeLine -> native.scriptLine
		}

val Field.scriptField: ScriptField
	get() = name fieldTo value.script

val Function.scriptLine: ScriptLine
	get() =
		doingName lineTo body.script

val Body.script: Script
	get() =
		when (this) {
			is FnBody -> script("native")
			is BlockBody -> block.script
		}

val Block.script: Script
	get() =
		typedScriptOrNull ?: untypedScript

val Block.typedScriptOrNull: Script?
	get() =
		typeOrNull?.name?.let { script(it lineTo untypedScript) }

val Native.scriptLine: ScriptLine
	get() =
		nativeName lineTo script(any.toString())
