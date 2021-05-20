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
		functionName lineTo script

val Native.scriptLine: ScriptLine
	get() =
		nativeName lineTo script(any.toString())
