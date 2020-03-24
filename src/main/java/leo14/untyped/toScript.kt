package leo14.untyped

import leo13.recurseName
import leo14.*

val Value.script: Script
	get() =
		when (this) {
			EmptyValue -> script()
			is SequenceValue -> sequence.script
		}

val Sequence.script
	get() =
		script(scriptLink)

val Sequence.scriptLink
	get() =
		tail.script linkTo head.scriptLine

val Line.scriptLine: ScriptLine
	get() =
		when (this) {
			is LiteralLine -> literal.scriptLine
			is FieldLine -> field.scriptLine
			is FunctionLine -> function.scriptLine
			is NativeLine -> nativeName lineTo script(literal(native.toString()))
		}

val Literal.scriptLine: ScriptLine
	get() =
		scriptLine(this)

val Field.scriptLine
	get() =
		line(scriptField)

val Field.scriptField
	get() =
		name fieldTo thunk.script

val Thunk.script
	get() =
		when (this) {
			is ValueThunk -> value.script
			is LazyThunk -> lazy.printScript
		}

val Function.bodyScript
	get() =
		context.functionScript.plus(script)

val Function.scriptLine
	get() =
		functionName lineTo script

val Context.reflectScriptLine: ScriptLine
	get() =
		"context" lineTo functionScript

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
		thunk.script

val Body.ruleScriptLine
	get() =
		when (this) {
			is ThunkBody -> givesName lineTo thunk.script
			is ScriptBody -> doesName lineTo script
			is RecurseBody -> recurseName lineTo script
		}

// === scriptOrNull

val Value.scriptOrNull: Script?
	get() =
		when (this) {
			EmptyValue -> script()
			is SequenceValue -> sequence.scriptLinkOrNull?.let { script(it) }
		}

val Sequence.scriptLinkOrNull: ScriptLink?
	get() =
		tail.value.scriptOrNull?.let { script ->
			head.scriptLineOrNull?.let { line ->
				script linkTo line
			}
		}

val Line.scriptLineOrNull: ScriptLine?
	get() =
		when (this) {
			is LiteralLine -> literal.scriptLine
			is FieldLine -> field.scriptFieldOrNull?.let { line(it) }
			is FunctionLine -> null
			is NativeLine -> null
		}

val Field.scriptFieldOrNull: ScriptField?
	get() =
		name fieldTo thunk.script
