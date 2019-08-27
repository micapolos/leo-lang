package leo13.type.pattern

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Pattern

data class EmptyPattern(val empty: Empty) : Pattern()
data class ChoicePattern(val choice: Choice) : Pattern()
data class ArrowPattern(val arrow: PatternArrow) : Pattern()
data class LinkPattern(val link: PatternLink) : Pattern()
data class TypePattern(val type: Type) : Pattern()

fun pattern(empty: Empty): Pattern = EmptyPattern(empty)
fun pattern(choice: Choice): Pattern = ChoicePattern(choice)
fun pattern(arrow: PatternArrow): Pattern = ArrowPattern(arrow)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)
fun pattern(type: Type): Pattern = TypePattern(type)

fun Pattern.plus(vararg lines: PatternLine) = fold(lines) { pattern(link(this, it)) }
fun pattern(vararg lines: PatternLine) = pattern(empty).plus(*lines)
fun pattern(name: String) = pattern(name lineTo pattern())

fun Pattern.contains(pattern: Pattern): Boolean =
	when (this) {
		is EmptyPattern -> pattern is EmptyPattern
		is ChoicePattern -> choice.contains(pattern)
		is ArrowPattern -> pattern is ArrowPattern && arrow.contains(pattern.arrow)
		is LinkPattern -> pattern is LinkPattern && link.contains(pattern.link) // TODO line should contain single case
		is TypePattern -> pattern is TypePattern && type.contains(pattern.type)
	}
