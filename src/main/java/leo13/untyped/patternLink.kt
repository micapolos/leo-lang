package leo13.untyped

data class PatternLink(val lhs: Pattern, val choice: Choice)

infix fun Pattern.linkTo(choice: Choice) = PatternLink(this, choice)