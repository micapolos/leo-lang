package leo7.frp

data class NumberIfFalse(val number: Number)

val Number.ifFalse get() = NumberIfFalse(this)
