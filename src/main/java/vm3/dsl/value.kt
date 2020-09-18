package vm3.dsl

import vm3.Fn
import vm3.Type
import vm3.Value

val Boolean.value: Value get() = Value.Bool(this)
val Int.value: Value get() = Value.I32(this)
val Float.value: Value get() = Value.F32(this)
val input: Value = Value.Input

val Value.inc: Value get() = Value.Inc(this)
val Value.dec: Value get() = Value.Dec(this)

operator fun Value.plus(rhs: Value): Value = Value.Plus(this, rhs)
operator fun Value.minus(rhs: Value): Value = Value.Minus(this, rhs)

fun Type.fn(fn: Value.() -> Value): Fn =
	Fn(this, input.fn())