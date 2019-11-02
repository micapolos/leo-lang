package leo13.lambda

sealed class Value<out T>

data class NativeValue<T>(val native: T) : Value<T>()
data class AbstractionValue<T>(val abstraction: Abstraction<Value<T>>) : Value<T>()
data class ApplicationValue<T>(val application: Application<Value<T>>) : Value<T>()
data class VariableValue<T>(val variable: Variable<T>) : Value<T>()

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(abstraction: Abstraction<Value<T>>): Value<T> = AbstractionValue(abstraction)
fun <T> value(application: Application<Value<T>>): Value<T> = ApplicationValue(application)
fun <T> value(variable: Variable<T>): Value<T> = VariableValue(variable)
