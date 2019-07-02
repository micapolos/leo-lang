package leo7.frp

data class NumberPerSecond(val number: Number, val second: Second)

infix fun Number.per(second: Second) = NumberPerSecond(this, second)
