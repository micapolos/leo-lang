package vm3.dsl

import vm3.Value

val Boolean.value: Value get() = Value.Bool(this)
val Int.value: Value get() = Value.I32(this)
val Float.value: Value get() = Value.F32(this)
val input: Value = Value.Input

val Value.intInc: Value get() = Value.I32Inc(this)
val Value.intDec: Value get() = Value.I32Dec(this)

fun Value.intPlus(rhs: Value): Value = Value.I32Plus(this, rhs)
