package leo8

data class IntegerStart(val integer: Integer)

val Integer.start get() = IntegerStart(this)
