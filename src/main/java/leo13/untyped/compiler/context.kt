package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.pattern.*
import leo9.Stack
import leo9.stack

data class Context(
	val arrowStack: Stack<PatternArrow>,
	val givenPattern: Pattern) : ObjectScripting() {
	override val scriptingLine
		get() =
			"context" lineTo script(
				"arrows" lineTo arrowStack.script { scriptLine }.emptyIfEmpty,
				"binding" lineTo givenPattern.bodyScript)
}

fun context(vararg arrows: PatternArrow) = Context(stack(*arrows), pattern())

fun Context.bind(pattern: Pattern) =
	Context(arrowStack, givenPattern.plus("given" lineTo pattern))
