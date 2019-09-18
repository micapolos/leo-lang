package leo13.untyped.pattern

import leo.base.ifOrNull

data class PatternLink(val lhs: Pattern, val item: PatternItem)

infix fun Pattern.linkTo(item: PatternItem) = PatternLink(this, item)

val PatternLink.pattern get() =
	lhs.plus(item)

fun PatternLink.leafPlusOrNull(pattern: Pattern): PatternLink? =
	ifOrNull(lhs.isEmpty) {
		item.leafPlusOrNull(pattern)?.let { lhs linkTo it }
	}
