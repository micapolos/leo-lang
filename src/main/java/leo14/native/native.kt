package leo14.native

import leo.base.notNullIf
import leo14.Number
import leo14.lambda.*
import leo14.number
import java.math.BigDecimal

sealed class Native

data class BooleanNative(val boolean: Boolean) : Native()
data class StringNative(val string: String) : Native()
data class NumberNative(val number: Number) : Native()
data class SwitchNative(val falseTerm: Term<Native>, val trueTerm: Term<Native>) : Native()
object NumberIsZeroNative : Native()
object NumberDecNative : Native()
object NumberIncNative : Native()
object NumberPlusIntNative : Native()
object NumberTimesIntNative : Native()
object NumberPlusDoubleNative : Native()
object StringEqualsStringNative : Native()
object StringPlusStringNative : Native()
data class EqualsStringNative(val string: String) : Native()
data class PlusNumberNative(val number: Number) : Native()
data class PlusStringNative(val string: String) : Native()
data class TimesDoubleNative(val number: Number) : Native()
object LogNative : Native()

fun native(boolean: Boolean): Native = BooleanNative(boolean)
fun native(string: String): Native = StringNative(string)
fun native(number: Number): Native = NumberNative(number)
fun native(double: Double): Native = native(number(double))
fun native(int: Int): Native = native(number(int))
val numberIsZeroNative: Native = NumberIsZeroNative
val numberDecNative: Native = NumberDecNative
val numberIncNative: Native = NumberIncNative
val numberTimesIntNative: Native = NumberTimesIntNative
val numberPlusNumberNative: Native = NumberPlusDoubleNative
val stringEqualsStringNative: Native = StringEqualsStringNative
val stringPlusStringNative: Native = StringPlusStringNative
fun switchNative(falseTerm: Term<Native>, trueTerm: Term<Native>): Native = SwitchNative(falseTerm, trueTerm)

fun plusNative(int: Number): Native = PlusNumberNative(int)
fun timesNative(int: Number): Native = TimesDoubleNative(int)
fun equalsNative(string: String): Native = EqualsStringNative(string)
fun plusNative(string: String): Native = PlusStringNative(string)
val logNative: Native = LogNative

val Native.boolean get() = (this as BooleanNative).boolean
val Native.number get() = (this as NumberNative).number
val Native.string get() = (this as StringNative).string

fun Native.invoke(value: Value<Native>): Value<Native>? =
	notNullIf(value.term is NativeTerm) {
		value(value.scope, invoke(value.term.native))
	}

val nativeEvaluator = evaluator(Native::invoke)

fun Native.invoke(native: Native): Term<Native> =
	when (this) {
		is BooleanNative -> null
		is StringNative -> null
		is NumberNative -> null
		is SwitchNative -> if (native.boolean) falseTerm else trueTerm
		is NumberIsZeroNative -> term(native(native.number.bigDecimal == BigDecimal.ZERO))
		is NumberIncNative -> term(native(number(native.number.bigDecimal.inc())))
		is NumberDecNative -> term(native(number(native.number.bigDecimal.dec())))
		is NumberPlusIntNative -> term(plusNative(native.number))
		is NumberTimesIntNative -> term(timesNative(native.number))
		is NumberPlusDoubleNative -> term(plusNative(native.number))
		is StringEqualsStringNative -> term(equalsNative(native.string))
		is StringPlusStringNative -> term(plusNative(native.string))
		is EqualsStringNative -> term(native(string == native.string))
		is PlusNumberNative -> term(native(number(number.bigDecimal + native.number.bigDecimal)))
		is TimesDoubleNative -> term(native(number(number.bigDecimal * native.number.bigDecimal)))
		is PlusNumberNative -> term(native(number(number.bigDecimal + native.number.bigDecimal)))
		is PlusStringNative -> term(native(string + native.string))
		is LogNative -> term(native.also { println(it) })
	} ?: error("$this.invoke($native)")