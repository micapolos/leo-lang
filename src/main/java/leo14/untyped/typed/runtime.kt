package leo14.untyped.typed

import leo14.untyped.Value

val runtimeStack = arrayListOf<Value>()
inline val runtimeTopIndex get() = runtimeStack.size.dec()
inline val runtimeTop get() = runtimeStack[runtimeTopIndex]
inline val runtimePop get() = runtimeStack.removeAt(runtimeTopIndex)
inline fun runtimeUpdateTop(fn: (Value) -> Value) = runtimeTopIndex.let { index -> runtimeStack[index] = fn(runtimeStack[index]) }
