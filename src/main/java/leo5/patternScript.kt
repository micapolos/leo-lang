package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class PatternScript

data class EmptyPatternScript(val empty: Empty) : PatternScript()
data class ApplicationPatternScript(val application: PatternApplication) : PatternScript()

fun patternScript(empty: Empty): PatternScript = EmptyPatternScript(empty)
fun script(application: PatternApplication): PatternScript = ApplicationPatternScript(application)
fun PatternScript.apply(dictionary: PatternDictionary) = script(application(pattern(this), dictionary))
fun patternScript(vararg dictionaries: PatternDictionary) = patternScript(empty).fold(dictionaries) { apply(it) }

fun PatternScript.contains(script: Script): Boolean = when (this) {
	is EmptyPatternScript -> script is EmptyScript
	is ApplicationPatternScript -> script is ApplicationScript && application.contains(script.application)
}
