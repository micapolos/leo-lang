package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.pattern.*

data class Context(
	val arrows: PatternArrows,
	val givenPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				arrows.scriptingLine,
				"binding" lineTo givenPattern.bodyScript)
}

fun context() = Context(patternArrows(), pattern())

fun Context.plus(arrow: PatternArrow) =
	Context(arrows.plus(arrow), givenPattern)

fun Context.bind(pattern: Pattern) =
	Context(arrows, givenPattern.plus("given" lineTo pattern))
