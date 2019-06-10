package leo5.compiler

import leo.base.Empty

sealed class Layout

data class EmptyLayout(val empty: Empty) : Layout()
data class StructLayout(val struct: Struct) : Layout()
data class ArrayLayout(val array: Array) : Layout()

fun layout(empty: Empty): Layout = EmptyLayout(empty)
fun layout(struct: Struct): Layout = StructLayout(struct)
fun layout(array: Array): Layout = ArrayLayout(array)