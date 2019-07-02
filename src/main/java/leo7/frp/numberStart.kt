package leo7.frp

data class NumberStart(val number: Number)

val Number.start get() = NumberStart(this)
fun start(number: Number) = number.start
