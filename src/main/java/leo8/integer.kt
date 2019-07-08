package leo8

sealed class Integer

data class IntInteger(val int: Int) : Integer()
data class IncrementInteger(val increment: IntegerIncrement) : Integer()
data class PlusInteger(val plus: IntegerPlus) : Integer()

val Int.integer: Integer get() = IntInteger(this)
val IntegerIncrement.integer: Integer get() = IncrementInteger(this)
val IntegerPlus.integer: Integer get() = PlusInteger(this)
