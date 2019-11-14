package leo14.typed.compiler

import leo14.typed.Action
import leo14.typed.Type

sealed class MemoryItem<T>
data class RememberMemoryItem<T>(val action: Action<T>) : MemoryItem<T>()
data class ForgetMemoryItem<T>(val type: Type) : MemoryItem<T>()
