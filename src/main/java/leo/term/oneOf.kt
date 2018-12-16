package leo.term

import leo.base.Stack
import leo.base.stack

data class OneOf(
	val patternStack: Stack<Pattern>)

val Stack<Pattern>.oneOf: OneOf
	get() =
		OneOf(this)

fun oneOf(pattern: Pattern, vararg patterns: Pattern): OneOf =
	stack(pattern, *patterns).oneOf