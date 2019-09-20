package leo13.untyped.compiler

import leo.base.notNullIf
import leo13.*
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.untyped.pattern.Pattern
import leo13.untyped.pattern.contains

data class Patterns(val stack: Stack<Pattern>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "patterns" lineTo stack.scripting.script.emptyIfEmpty
	fun plus(pattern: Pattern) = Patterns(stack.push(pattern))

	fun resolve(compiled: Compiled): Compiled =
		stack.mapFirst {
			notNullIf(contains(compiled.pattern)) {
				compiled(compiled.expression, this)
			}
		} ?: compiled
}

fun patterns() = Patterns(stack())

