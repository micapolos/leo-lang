package leo7.frp

data class NumberStep(val number: Number)

val Number.step get() = NumberStep(this)
