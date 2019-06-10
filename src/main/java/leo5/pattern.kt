package leo5

import leo5.core.Value as CoreValue

sealed class Pattern

data class ScriptPattern(val script: PatternScript) : Pattern()
data class FunctionPattern(val function: PatternFunction) : Pattern()

fun pattern(script: PatternScript): Pattern = ScriptPattern(script)
fun pattern(function: PatternFunction): Pattern = FunctionPattern(function)

fun Pattern.apply(dictionary: PatternDictionary) = pattern(script(application(this, dictionary)))
fun pattern(vararg dictionaries: PatternDictionary) = pattern(patternScript(*dictionaries))

fun Pattern.contains(value: Value): Boolean = when (this) {
	is ScriptPattern -> value is ScriptValue && script.contains(value.script)
	is FunctionPattern -> value is FunctionValue && value.function.pattern == function.pattern
}
