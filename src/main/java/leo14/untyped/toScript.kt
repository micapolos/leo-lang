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
		name fieldTo rhs.script

val Function.bodyScript
	get() =
		context.functionScript.plus(script)

val Function.scriptLine
	get() =
		"function" lineTo script(
			"context" lineTo context.functionScript,
			"body" lineTo script)

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
		program.script

val Body.ruleScriptLine
	get() =
		when (this) {
			is ProgramBody -> "is" lineTo program.script
			is FunctionBody -> "does" lineTo function.bodyScript
		}

// === scriptOrNull

val Program.scriptOrNull: Script?
	get() =
		when (this) {
			EmptyProgram -> script()
			is SequenceProgram -> sequence.scriptLinkOrNull?.let { script(it) }
		}

val Sequence.scriptLinkOrNull: ScriptLink?
	get() =
		tail.scriptOrNull?.let { script ->
			head.scriptLineOrNull?.let { line ->
				script linkTo line
			}
		}

val Value.scriptLineOrNull: ScriptLine?
	get() =
		when (this) {
			is LiteralValue -> line(literal)
			is FieldValue -> field.scriptFieldOrNull?.let { line(it) }
			is FunctionValue -> null
		}

val Field.scriptFieldOrNull: ScriptField?
	get() =
		rhs.scriptOrNull?.let { rhs -> name fieldTo rhs }
