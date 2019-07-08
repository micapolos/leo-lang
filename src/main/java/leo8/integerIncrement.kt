package leo8

data class IntegerIncrement(val integer: Integer)

val Integer.increment get() = IntegerIncrement(this)