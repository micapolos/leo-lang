package leo13.untyped.pattern

data class PatternLink(val lhs: Pattern, val item: PatternItem)

infix fun Pattern.linkTo(item: PatternItem) = PatternLink(this, item)