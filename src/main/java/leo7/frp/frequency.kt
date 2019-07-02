package leo7.frp

data class Frequency(val times: NumberTimes, val second: Second)

infix fun NumberTimes.per(second: Second) = Frequency(this, second)
