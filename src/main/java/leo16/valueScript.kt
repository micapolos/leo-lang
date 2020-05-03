package leo16

import leo13.array
import leo13.map
import leo14.*

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
			is FunctionField -> function.printSentence.scriptLine
			is DictionaryField -> dictionary.printSentence.scriptLine
			is NativeField -> native.nativeScriptLine
		}

val Field.textScriptLineOrNull: ScriptLine?
	get() =
		matchText { it.literal.line }

val Field.numberScriptLineOrNull: ScriptLine?
	get() =
		matchNumber { literal(number(it)).line }

val Sentence.scriptLine: ScriptLine
	get() =
		word lineTo value.script

val Any?.nativeScriptLine: ScriptLine
	get() =
		nativeString.line