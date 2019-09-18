package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.pattern.*

data class Context(
	val arrows: PatternArrows,
	val functions: Functions,
	val givenPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				arrows.scriptingLine,
				functions.scriptingLine,
				"binding" lineTo givenPattern.bodyScript.emptyIfEmpty)
}

fun context() = Context(patternArrows(), functions(), pattern())

fun Context.plus(arrow: PatternArrow) =
	Context(arrows.plus(arrow), functions, givenPattern)

fun Context.plus(function: FunctionCompiled) =
	Context(arrows, functions.plus(function), givenPattern)

fun Context.bind(pattern: Pattern) =
	Context(arrows, functions, givenPattern.plus("given" lineTo pattern))
