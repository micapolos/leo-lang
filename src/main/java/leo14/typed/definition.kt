package leo14.typed

sealed class Binding<T>
data class FunctionBinding<T>(val action: Action<T>) : Binding<T>()
data class TypedBinding<T>(val type: Type) : Binding<T>()