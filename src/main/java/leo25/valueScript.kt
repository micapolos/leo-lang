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
		}

val Field.scriptField: ScriptField
	get() = name fieldTo value.script

val Function.scriptLine: ScriptLine
	get() =
		"function" lineTo script