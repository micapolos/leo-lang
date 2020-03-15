package leo14.untyped

import leo14.*

val Program.script: Script
	get() =
		when (this) {
			EmptyProgram -> script()
			is SequenceProgram -> sequence.script
		}

val Sequence.script
	get() =
		script(scriptLink)

val Sequence.scriptLink
	get() =
		tail.script linkTo head.scriptLine

val Value.scriptLine
	get() =
		when (this) {
			is LiteralValue -> literal.scriptLine
			is FieldValue -> field.scriptLine
			is FunctionValue -> function.scriptLine
		}

val Literal.scriptLine
	get() =
		line(this)

val Field.scriptLine
	get() =
		line(scriptField)

val Field.scriptField
	get() =
		if (name in keywords) "meta" fieldTo script(rawScriptField)
		else rawScriptField

val Field.rawScriptField
	get() =
		name fieldTo rhs.script

val keywords = setOf("function")

val Function.bodyScript
	get() =
		context.functionScript.plus(script)

val Function.scriptLine
	get() =
		"function" lineTo context.functionScript.plus(script)

val Context.functionScript: Script
	get() =
		when (this) {
			EmptyContext -> script()
			is NonEmptyContext -> parentContext.functionScript.plus(lastRule.contextScript)
		}

val Rule.contextScript
	get() =
		pattern.ruleScript.plus(body.ruleScriptLine)

val Pattern.ruleScript
	get() =
		script(scriptLink)

val Body.ruleScriptLine
	get() =
		when (this) {
			is ScriptBody -> "gives" lineTo script
			is FunctionBody -> "does" lineTo function.bodyScript
		}
