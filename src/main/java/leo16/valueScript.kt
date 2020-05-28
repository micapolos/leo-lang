package leo16

import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.invoke
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.number
import leo14.script
import leo16.names.*

val Value.script: Script
	get() =
		script(*fieldStack.map { scriptLine }.array)

val Field.scriptLine: ScriptLine
	get() =
		null
			?: textScriptLineOrNull
			?: numberScriptLineOrNull
			?: defaultScriptLine

val Field.defaultScriptLine: ScriptLine
	get() =
		when (this) {
			is SentenceField -> sentence.scriptLine
			is FunctionField -> function.scriptLine
			is NativeField -> native.nativeScriptLine
			is ChoiceField -> choice.scriptLine
			is LazyField -> lazy.scriptLine
			is EvaluatedField -> evaluated.scriptLine
		}

val Field.textScriptLineOrNull: ScriptLine?
	get() =
		matchText { it.literal.line }

val Field.numberScriptLineOrNull: ScriptLine?
	get() =
		matchNumber { literal(number(it)).line }

val Sentence.scriptLine: ScriptLine
	get() =
		word.scriptWord lineTo value.script

val Function.scriptLine: ScriptLine
	get() =
		_taking(pattern.asValue.script)

val Choice.scriptLine: ScriptLine
	get() =
		_choice(eitherStack.map { scriptLine }.script)

val Either.scriptLine: ScriptLine
	get() =
		_either(field.scriptLine)

val Lazy.scriptLine: ScriptLine
	get() =
		_lazy(compiled.bodyValue.script)

val Evaluated.scriptLine: ScriptLine
	get() =
		_evaluated(scope.asField, value.asField)

val String.scriptWord: String
	get() =
		when (this) {
			_item -> "·"
			else -> this
		}

val Any?.nativeScriptLine: ScriptLine
	get() =
		nativeString.line
