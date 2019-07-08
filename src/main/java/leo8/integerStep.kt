package leo8

data class IntegerStep(val integer: Integer)

val Integer.step get() = IntegerStep(this)
