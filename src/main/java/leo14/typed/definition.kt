package leo14.typed

sealed class Binding<T>
data class FunctionBinding<T>(val function: Function<T>) : Binding<T>()
data class TypedBinding<T>(val type: Type) : Binding<T>()