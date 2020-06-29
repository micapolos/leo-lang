package leo16

import leo14.Script
import leo14.ScriptLine
import leo14.emptyScript
import leo14.invoke
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo14.untyped.scriptLine
import leo16.names.*

val Value.script: Script
	get() =
		null
			?: numberScriptOrNull
			?: textScriptOrNull
			?: defaultScript

val Value.defaultScript: Script
	get() =
		when (this) {
			EmptyValue -> emptyScript
			is LinkValue -> link.script
			is FunctionValue -> function.script
			is LazyValue -> lazy.script
			is NativeValue -> native.nativeScript
			is FuncValue -> TODO()
		}

val Value.textScriptOrNull: Script?
	get() =
		matchText { string ->
			string.literal.scriptLine.script
		}

val Value.numberScriptOrNull: Script?
	get() =
		matchNumber { bigDecimal ->
			bigDecimal.literal.scriptLine.script
		}

val ValueLink.script: Script
	get() =
		previousValue.script.plus(lastSentence.scriptLine)

val Sentence.scriptLine: ScriptLine
	get() =
		word.scriptWord lineTo rhsValue.script

val Function.script: Script
	get() =
		_taking(patternValue.script).script

val Lazy.script: Script
	get() =
		_lazy(compiled.bodyValue.script).script

val String.scriptWord: String
	get() =
		when (this) {
			//_item -> "·"
			else -> this
		}

val Any?.nativeScriptLine: ScriptLine
	get() =
		nativeString.line

val Any?.nativeScript: Script
	get() =
		nativeString.line.script
