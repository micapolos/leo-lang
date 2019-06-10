package leo5

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo5.core.Value as CoreValue

sealed class Pattern

data class EmptyPattern(val empty: Empty) : Pattern()
data class ApplicationPattern(val application: PatternApplication) : Pattern()
data class FunctionPattern(val function: PatternFunction) : Pattern()

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(application: PatternApplication): Pattern = ApplicationPattern(application)
fun pattern(function: PatternFunction): Pattern = FunctionPattern(function)

fun Pattern.apply(dictionary: PatternDictionary) = pattern(application(this, dictionary))
fun pattern(vararg dictionaries: PatternDictionary) = pattern(empty).fold(dictionaries, Pattern::apply)

fun Pattern.contains(value: Value): Boolean = when (this) {
	is EmptyPattern -> value is ScriptValue && value.script is EmptyScript
	is ApplicationPattern -> value is ScriptValue && value.script is ApplicationScript && application.contains(value.script.application)
	is FunctionPattern -> value is FunctionValue && value.function.pattern == function.pattern
}
