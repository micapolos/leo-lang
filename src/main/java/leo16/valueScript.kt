package leo16

import leo13.array
import leo13.map
import leo14.*
import leo15.choiceName
import leo15.takingName

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
			is TakingField -> taking.scriptLine
			is DictionaryField -> dictionary.printSentence.scriptLine
			is NativeField -> native.nativeScriptLine
			is ChoiceField -> choice.scriptLine
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

val Taking.scriptLine: ScriptLine
	get() =
		takingName(pattern.asValue.script)

val Choice.scriptLine: ScriptLine
	get() =
		choiceName(caseFieldStack.map { scriptLine })

val String.scriptWord: String
	get() =
		when (this) {
			//itemName -> "-"
			else -> this
		}

val Any?.nativeScriptLine: ScriptLine
	get() =
		nativeString.line