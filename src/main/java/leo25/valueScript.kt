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
		when (this) {
			is FieldLine -> line(field.scriptField)
			is FunctionLine -> function.scriptLine
			is LiteralLine -> leo14.line(literal)
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
