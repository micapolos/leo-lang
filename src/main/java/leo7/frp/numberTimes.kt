package leo7.frp

data class NumberTimes(val number: Number)

val Number.times get() = NumberTimes(this)
