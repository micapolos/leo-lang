package leo14.native

import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.term

sealed class Native

data class BooleanNative(val boolean: Boolean) : Native()
data class StringNative(val string: String) : Native()
data class DoubleNative(val double: Double) : Native()
data class SwitchNative(val falseTerm: Term<Native>, val trueTerm: Term<Native>) : Native()
object DoubleIsZeroNative : Native()
object DoubleDecNative : Native()
object DoubleIncNative : Native()
object DoublePlusIntNative : Native()
object DoubleTimesIntNative : Native()
object DoublePlusDoubleNative : Native()
object StringEqualsStringNative : Native()
data class EqualsStringNative(val string: String) : Native()
data class PlusDoubleNative(val double: Double) : Native()
data class TimesDoubleNative(val double: Double) : Native()
object LogNative : Native()

fun native(boolean: Boolean): Native = BooleanNative(boolean)
fun native(string: String): Native = StringNative(string)
fun native(double: Double): Native = DoubleNative(double)
fun native(int: Int): Native = native(int.toDouble())
val doubleIsZeroNative: Native = DoubleIsZeroNative
val doubleDecNative: Native = DoubleDecNative
val doubleIncNative: Native = DoubleIncNative
val doubleTimesIntNative: Native = DoubleTimesIntNative
val doublePlusDoubleNative: Native = DoublePlusDoubleNative
val stringEqualsStringNative: Native = StringEqualsStringNative
fun switchNative(falseTerm: Term<Native>, trueTerm: Term<Native>): Native = SwitchNative(falseTerm, trueTerm)

fun plusNative(int: Double): Native = PlusDoubleNative(int)
fun timesNative(int: Double): Native = TimesDoubleNative(int)
fun equalsNative(string: String): Native = EqualsStringNative(string)
val logNative: Native = LogNative

val Native.boolean get() = (this as BooleanNative).boolean
val Native.double get() = (this as DoubleNative).double
val Native.string get() = (this as StringNative).string

fun Native.invoke(term: Term<Native>): Term<Native> =
	if (term is NativeTerm) invoke(term.native)
	else error("$this.invoke($term)")

fun Native.invoke(native: Native): Term<Native> =
	when (this) {
		is BooleanNative -> null
		is StringNative -> null
		is DoubleNative -> null
		is SwitchNative -> if (native.boolean) falseTerm else trueTerm
		is DoubleIsZeroNative -> term(native(native.double == 0.0))
		is DoubleIncNative -> term(native(native.double.inc()))
		is DoubleDecNative -> term(native(native.double.dec()))
		is DoublePlusIntNative -> term(plusNative(native.double))
		is DoubleTimesIntNative -> term(timesNative(native.double))
		is DoublePlusDoubleNative -> term(plusNative(native.double))
		is StringEqualsStringNative -> term(equalsNative(native.string))
		is EqualsStringNative -> term(native(string == native.string))
		is PlusDoubleNative -> term(native(double + native.double))
		is TimesDoubleNative -> term(native(double * native.double))
		is PlusDoubleNative -> term(native(double + native.double))
		is LogNative -> term(native.also { println(it) })
	} ?: error("$this.invoke($native)")