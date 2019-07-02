package leo7.frp

data class NumberIfTrue(val number: Number)

val Number.ifTrue get() = NumberIfTrue(this)
