package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.script
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.scripting
import leo13.untyped.pattern.Pattern
import leo13.untyped.pattern.PatternArrow
import leo13.untyped.pattern.contains
import leo13.untyped.pattern.rhsOrNull
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

data class PatternArrows(val arrowStack: Stack<PatternArrow>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "arrows" lineTo arrowStack.scripting.script.emptyIfEmpty
}

fun patternArrows() = PatternArrows(stack())

fun PatternArrows.plus(arrow: PatternArrow) =
	PatternArrows(arrowStack.push(arrow))

fun PatternArrows.rhsOrNull(pattern: Pattern): Pattern? =
	arrowStack.mapFirst { rhsOrNull(pattern) }

fun PatternArrows.resolve(pattern: Pattern): Pattern =
	rhsOrNull(pattern) ?: pattern

fun PatternArrows.resolve(compiled: Compiled): Compiled =
	arrowStack.mapFirst {
		notNullIf(rhs.contains(compiled.pattern)) {
			compiled(compiled.expression, rhs)
		}
	} ?: compiled