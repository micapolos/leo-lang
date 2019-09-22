package leo13.compiler

import leo13.ObjectScripting
import leo13.pattern.Pattern
import leo13.pattern.PatternLine
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script

data class Context(
	val patternDefinitions: PatternDefinitions,
	val patternLines: PatternLines,
	val functions: Functions,
	val givenPattern: Pattern,
	val switchedPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				patternDefinitions.scriptingLine,
				patternLines.scriptingLine,
				functions.scriptingLine,
				"given" lineTo script(givenPattern.scriptingLine),
				"switched" lineTo script(switchedPattern.scriptingLine))
}

fun context() = Context(patternDefinitions(), patternLines(), functions(), pattern(), pattern())

fun Context.plus(definition: PatternDefinition) =
	copy(patternDefinitions = patternDefinitions.plus(definition))

fun Context.plus(line: PatternLine) =
	copy(patternLines = patternLines.plus(line))

fun Context.plus(function: FunctionCompiled) =
	copy(functions = functions.plus(function))

fun Context.give(pattern: Pattern) =
	copy(givenPattern = givenPattern.plus("given" lineTo pattern))

fun Context.switch(pattern: Pattern) =
	copy(switchedPattern = switchedPattern.plus("switched" lineTo pattern))
