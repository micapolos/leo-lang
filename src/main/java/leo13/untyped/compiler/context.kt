package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.pattern.*

data class Context(
	val patternDefinitions: PatternDefinitions,
	val patterns: Patterns,
	val functions: Functions,
	val givenPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				patternDefinitions.scriptingLine,
				patterns.scriptingLine,
				functions.scriptingLine,
				"binding" lineTo givenPattern.bodyScript.emptyIfEmpty)
}

fun context() = Context(patternDefinitions(), patterns(), functions(), pattern())

fun Context.plus(definition: PatternDefinition) =
	copy(patternDefinitions = patternDefinitions.plus(definition))

fun Context.plus(pattern: Pattern) =
	copy(patterns = patterns.plus(pattern))

fun Context.plus(function: FunctionCompiled) =
	copy(functions = functions.plus(function))

fun Context.bind(pattern: Pattern) =
	copy(givenPattern = givenPattern.plus("given" lineTo pattern))
