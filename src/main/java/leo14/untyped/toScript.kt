package leo14.untyped

import leo13.recurseName
import leo13.recursiveName
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
			is NativeValue -> nativeName lineTo script(literal(native.toString()))
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
		functionName lineTo
			if (recursive) script(recursiveName lineTo script)
			else script

val Context.functionScript: Script
	get() =
		when (this) {
			is EmptyContext -> script()
			is LinkContext -> link.functionScript
		}

val ContextLink.functionScript: Script
	get() =
		context.functionScript.plus(definition.contextScript)

val Rule.contextScript
	get() =
		pattern.ruleScript.plus(body.ruleScriptLine)

val Definition.contextScript
	get() =
		when (this) {
			is RuleDefinition -> rule.contextScript
			is RecursiveDefinition -> recursive.script
		}

val Recursive.script
	get() =
		script(recurseName lineTo context.functionScript)

val Pattern.ruleScript
	get() =
		program.script

val Body.ruleScriptLine
	get() =
		when (this) {
			is ProgramBody -> givesName lineTo program.script
			is ScriptBody -> doesName lineTo script
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
			is NativeValue -> null
		}

val Field.scriptFieldOrNull: ScriptField?
	get() =
		rhs.scriptOrNull?.let { rhs -> name fieldTo rhs }
