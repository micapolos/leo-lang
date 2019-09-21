package leo13.compiler

import leo13.ObjectScripting
import leo13.pattern.Pattern
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script

data class Context(
	val patternDefinitions: PatternDefinitions,
	val patterns: Patterns,
	val functions: Functions,
	val givenPattern: Pattern,
	val switchedPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				patternDefinitions.scriptingLine,
				patterns.scriptingLine,
				functions.scriptingLine,
				"given" lineTo script(givenPattern.scriptingLine),
				"switched" lineTo script(switchedPattern.scriptingLine))
}

fun context() = Context(patternDefinitions(), patterns(), functions(), pattern(), pattern())

fun Context.plus(definition: PatternDefinition) =
	copy(patternDefinitions = patternDefinitions.plus(definition))

fun Context.plus(pattern: Pattern) =
	copy(patterns = patterns.plus(pattern))

fun Context.plus(function: FunctionCompiled) =
	copy(functions = functions.plus(function))

fun Context.give(pattern: Pattern) =
	copy(givenPattern = givenPattern.plus("given" lineTo pattern))

fun Context.switch(pattern: Pattern) =
	copy(switchedPattern = switchedPattern.plus("switched" lineTo pattern))
