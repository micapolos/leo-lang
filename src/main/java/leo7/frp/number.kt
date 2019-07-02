package leo7.frp

sealed class Number

data class DoubleNumber(val double: Double) : Number()
data class OscillatorNumber(val oscillator: Oscillator) : Number()
data class AnimationNumber(val animation: Animation) : Number()
data class SwitchNumber(val switch: NumberSwitch) : Number()

val Double.number: Number get() = DoubleNumber(this)
val Oscillator.number: Number get() = OscillatorNumber(this)
val Animation.number: Number get() = AnimationNumber(this)
val NumberSwitch.number: Number get() = SwitchNumber(this)

fun number(int: Int) = int.toDouble().number
