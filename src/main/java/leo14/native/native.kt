package leo14.native

sealed class Native
data class StringNative(val string: String) : Native()
data class IntNative(val int: Int) : Native()
data class FunctionNative(val fn: Native.() -> Native) : Native()
