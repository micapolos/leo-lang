package gen

object Zero

val zero = Zero

object One

val one = One

sealed class Bit

data class ZeroBit(val zero: Zero) : Bit()
data class OneBit(val one: One) : Bit()

val Zero.bit: Bit get() = ZeroBit(this)
val One.bit: Bit get() = OneBit(this)

val Bit.negate
	get() = when (this) {
		is ZeroBit -> one.bit
		is OneBit -> zero.bit
	}

infix fun Bit.and(bit: Bit): Bit = when (this) {
	is ZeroBit -> when (bit) {
		is ZeroBit -> zero.bit
		is OneBit -> zero.bit
	}
	is OneBit -> when (bit) {
		is ZeroBit -> zero.bit
		is OneBit -> one.bit
	}
}

data class Percent(val float: Float)

val Float.percent get() = Percent(this)
val Int.percent get() = toFloat().percent

data class percent__red(val percent: Percent)
data class percent__green(val percent: Percent)
data class percent__blue(val percent: Percent)

fun red(percent: Percent) = percent__red(percent)
fun green(percent: Percent) = percent__green(percent)
fun blue(percent: Percent) = percent__blue(percent)

val Percent.red get() = red(this)
val Percent.green get() = green(this)
val Percent.blue get() = blue(this)

data class color(
	val red: percent__red,
	val green: percent__green,
	val blue: percent__blue)

val x = color(14.percent.red, 17.percent.green, 16.percent.blue)
val z = x.red.percent.float + x.green.percent.float