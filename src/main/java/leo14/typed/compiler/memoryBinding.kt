package leo14.typed.compiler

import leo14.typed.Typed

data class MemoryBinding<T>(val typed: Typed<T>, val isAction: Boolean)

fun <T> memoryBinding(typed: Typed<T>, isAction: Boolean) = MemoryBinding(typed, isAction)
