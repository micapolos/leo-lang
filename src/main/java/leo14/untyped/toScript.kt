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
		previousThunk.script linkTo lastLine.scriptLine

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
		scope.functionScript.plus(script)

val Function.scriptLine
	get() =
		functionName lineTo script

val Scope.reflectScriptLine: ScriptLine
	get() =
		scopeName lineTo functionScript

val Scope.functionScript: Script
	get() =
		when (this) {
			is EmptyScope -> script()
			is LinkScope -> link.functionScript
		}

val ScopeLink.functionScript: Script
	get() =
		scope.functionScript.plus(definition.scopeScript)

val Rule.scopeScript
	get() =
		pattern.ruleScript.plus(body.ruleScriptLine)

val Definition.scopeScript
	get() =
		when (this) {
			is RuleDefinition -> rule.scopeScript
		}

val Pattern.ruleScript
	get() =
		thunk.script

val Body.ruleScriptLine
	get() =
		when (this) {
			is ThunkBody -> isName lineTo thunk.script
			is GivesBody -> givesName lineTo script
			is DoesBody -> doesName lineTo script
			is RecurseBody -> recurseName lineTo script
			is MacroBody -> expandsName lineTo script
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
		previousThunk.value.scriptOrNull?.let { script ->
			lastLine.scriptLineOrNull?.let { line ->
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
