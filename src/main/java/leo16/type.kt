package leo16

import leo16.names.*

data class Type(val value: Value)

val Value.type get() = Type(this)
val emptyType = emptyValue.type
val nativeType = value(_any(_native())).type
val Type.isEmpty get() = value.isEmpty
val Type.normalize get() = value.normalize.type