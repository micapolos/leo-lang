package leo14.native

sealed class Native

data class BooleanNative(val boolean: Boolean) : Native()
data class StringNative(val string: String) : Native()
data class IntNative(val int: Int) : Native()
data class DoubleNative(val double: Double) : Native()
data class SwitchNative(val falseNative: Native, val trueNative: Native) : Native()
object IntIsZeroNative : Native()
object IntDecNative : Native()
object IntIncNative : Native()
object IntPlusIntNative : Native()
object DoublePlusDoubleNative : Native()
object StringEqualsStringNative : Native()
data class EqualsStringNative(val string: String) : Native()
data class PlusIntNative(val int: Int) : Native()
data class PlusDoubleNative(val double: Double) : Native()
object LogNative : Native()

fun native(boolean: Boolean): Native = BooleanNative(boolean)
fun native(string: String): Native = StringNative(string)
fun native(int: Int): Native = IntNative(int)
fun native(double: Double): Native = DoubleNative(double)
val intIsZeroNative: Native = IntIsZeroNative
val intDecNative: Native = IntIncNative
val intIncNative: Native = IntIncNative
val intPlusIntNative: Native = IntPlusIntNative
val doublePlusDoubleNative: Native = DoublePlusDoubleNative
val stringEqualsStringNative: Native = StringEqualsStringNative
fun switchNative(falseNative: Native, trueNative: Native): Native = SwitchNative(falseNative, trueNative)

fun plusNative(int: Int): Native = PlusIntNative(int)
fun plusNative(double: Double): Native = PlusDoubleNative(double)
fun equalsNative(string: String): Native = EqualsStringNative(string)
val logNative: Native = LogNative

val Native.boolean get() = (this as BooleanNative).boolean
val Native.int get() = (this as IntNative).int
val Native.double get() = (this as DoubleNative).double
val Native.string get() = (this as StringNative).string

fun Native.invoke(native: Native): Native =
	when (this) {
		is BooleanNative -> null
		is StringNative -> null
		is IntNative -> null
		is DoubleNative -> null
		is SwitchNative -> if (native.boolean) falseNative else trueNative
		is IntIsZeroNative -> native(native.int == 0)
		is IntIncNative -> native(native.int.inc())
		is IntDecNative -> native(native.int.dec())
		is IntPlusIntNative -> plusNative(native.int)
		is DoublePlusDoubleNative -> plusNative(native.double)
		is StringEqualsStringNative -> equalsNative(native.string)
		is EqualsStringNative -> native(string == native.string)
		is PlusIntNative -> native(int + native.int)
		is PlusDoubleNative -> native(double + native.double)
		is LogNative -> native.also { println(it) }
	} ?: error("$this.invoke($native)")