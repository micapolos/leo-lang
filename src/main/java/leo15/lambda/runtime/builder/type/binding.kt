package leo15.lambda.runtime.builder.type

sealed class Binding<out T>
data class TypeBinding<T>(val type: Type<T>) : Binding<T>()
data class ArrowBinding<T>(val arrow: Arrow<T>) : Binding<T>()

val <T> Type<T>.binding: Binding<T> get() = TypeBinding(this)
val <T> Arrow<T>.binding: Binding<T> get() = ArrowBinding(this)
