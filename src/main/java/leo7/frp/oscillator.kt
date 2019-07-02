package leo7.frp

data class Oscillator(val frequency: Frequency)

val Frequency.oscillator get() = Oscillator(this)
