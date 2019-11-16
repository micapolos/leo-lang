package leo14.native

sealed class Native

data class BooleanNative(val boolean: Boolean) : Native()
data class StringNative(val string: String) : Native()
data class IntNative(val int: Int) : Native()
data class SwitchNative(val falseNative: Native, val trueNative: Native) : Native()
object IntPlusIntNative : Native()
object StringEqualsStringNative : Native()
data class EqualsStringNative(val string: String) : Native()
data class PlusIntNative(val int: Int) : Native()

fun native(boolean: Boolean): Native = BooleanNative(boolean)
fun native(string: String): Native = StringNative(string)
fun native(int: Int): Native = IntNative(int)
val intPlusIntNative: Native = IntPlusIntNative
val stringEqualsStringNative: Native = StringEqualsStringNative
fun plusNative(int: Int): Native = PlusIntNative(int)
fun equalsNative(string: String): Native = EqualsStringNative(string)

fun Native.invoke(native: Native): Native =
	when (this) {
		is BooleanNative -> null
		is StringNative -> null
		is IntNative -> null
		is SwitchNative ->
			(native as? BooleanNative)?.boolean?.let { boolean ->
				if (boolean) falseNative else trueNative
			}
		is IntPlusIntNative ->
			(native as? IntNative)?.int?.let { int ->
				plusNative(int)
			}
		is StringEqualsStringNative ->
			(native as? StringNative)?.string?.let { string ->
				equalsNative(string)
			}
		is EqualsStringNative ->
			(native as? StringNative)?.string?.let { rhs ->
				native(string == rhs)
			}
		is PlusIntNative ->
			(native as? IntNative)?.int?.let { rhs ->
				native(int + rhs)
			}
	} ?: error("$this.invoke($native)")