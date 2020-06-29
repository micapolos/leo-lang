package leo16.base

data class Or<First, Second>(val first: First, val second: Second)

infix fun <First, Second> First.or(second: Second) = Or(this, second)
