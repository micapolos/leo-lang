package leo13.untyped.pattern

data class ChoiceLine(val name: String, val choice: Choice)
infix fun String.lineTo(choice: Choice) = ChoiceLine(this, choice)
