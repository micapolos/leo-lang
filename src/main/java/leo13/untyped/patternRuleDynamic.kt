package leo13.untyped

import leo13.script.*
import leo9.stack

sealed class PatternRuleDynamic

data class ChoicePatternRuleDynamic(val choice: Choice) : PatternRuleDynamic()
data class ScriptPatternRuleDynamic(val script: ObjectScript) : PatternRuleDynamic()

fun dynamic(choice: Choice): PatternRuleDynamic = ChoicePatternRuleDynamic(choice)
fun dynamic(script: ObjectScript): PatternRuleDynamic = ScriptPatternRuleDynamic(script)

val patternRuleDynamicName = "dynamic"

val dynamicPatternRuleNames = stack(choiceName, scriptName)

val patternRuleDynamicReader: Reader<PatternRuleDynamic> =
	reader(
		patternRuleDynamicName,
		case(choiceReader, ::dynamic),
		case(objectScriptReader, ::dynamic))

val patternRuleDynamicWriter: Writer<PatternRuleDynamic> =
	writer(patternRuleDynamicName) {
		when (this) {
			is ChoicePatternRuleDynamic -> choiceWriter.script(choice)
			is ScriptPatternRuleDynamic -> objectScriptWriter.script(script)
		}
	}

fun PatternRuleDynamic.matches(script: Script): Boolean =
	when (this) {
		is ChoicePatternRuleDynamic -> choice.matches(script)
		is ScriptPatternRuleDynamic -> this.script.matches(script)
	}
