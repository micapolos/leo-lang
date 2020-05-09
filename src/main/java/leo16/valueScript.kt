package leo16

import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.invoke
import leo14.isEmpty
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.number
import leo14.plus
import leo14.script
import leo15.choiceName
import leo15.takingName
import leo16.names.*

val Value.script: Script
	get() =
		script(*fieldStack.map { scriptLine }.array)

val Field.scriptLine: ScriptLine
	get() =
		null
			?: textScriptLineOrNull
			?: numberScriptLineOrNull
			?: listScriptLineOrNull
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

fun Field.listBodyScriptOrNull(word: String): Script? =
	matchPrefix(word) { rhs ->
		rhs.onlyFieldOrNull?.sentenceOrNull?.let { sentence ->
			when (sentence.word) {
				_empty -> script()
				_linked -> sentence.value.matchInfix(_last) { lhs, last ->
					lhs.matchPrefix(_previous) { previous ->
						previous.onlyFieldOrNull?.listBodyScriptOrNull(word)?.plus(_item(last.script))
					}
				}
				else -> null
			}
		}
	}

val Field.listScriptLineOrNull: ScriptLine?
	get() =
		sentenceOrNull?.let { sentence ->
			listBodyScriptOrNull(sentence.word)?.let { script ->
				sentence.word(if (script.isEmpty) script(_empty) else script)
			}
		}
