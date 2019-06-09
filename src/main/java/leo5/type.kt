package leo5

sealed class Type

data class SelfType(val self: Self) : Type()
data class ValueType(val value: Value) : Type()
data class ReturnsType(val returns: Returns) : Type()

fun type(self: Self): Type = SelfType(self)
fun type(value: Value): Type = ValueType(value)
fun type(returns: Returns): Type = ReturnsType(returns)

val Type.isSelf get() = this is SelfType
val Type.valueOrNull get() = (this as? ValueType)?.value
