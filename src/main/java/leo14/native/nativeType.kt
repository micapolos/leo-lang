package leo14.native

sealed class NativeType
object BooleanNativeType : NativeType()
object StringNativeType : NativeType()
object IntNativeType : NativeType()
data class FunctionNativeType(val lhs: NativeType, val rhs: NativeType) : NativeType()
