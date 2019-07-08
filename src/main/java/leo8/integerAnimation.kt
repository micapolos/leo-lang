package leo8

data class IntegerAnimation(val start: IntegerStart, val step: IntegerStep)

fun animation(start: IntegerStart, step: IntegerStep) = IntegerAnimation(start, step)