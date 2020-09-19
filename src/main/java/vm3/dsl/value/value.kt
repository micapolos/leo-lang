package vm3.dsl.value

import vm3.Fn
import vm3.Type
import vm3.Value

val input: Value = Value.Input

val Boolean.value: Value get() = Value.Bool(this)
val Int.value: Value get() = Value.I32(this)
val Float.value: Value get() = Value.F32(this)

fun array(vararg values: Value): Value = Value.Array(values.toList())
operator fun Value.get(index: Value): Value = Value.ArrayAt(this, index)

fun struct(vararg fields: Pair<String, Value>): Value = Value.Struct(fields.map { it.field })
operator fun Value.get(name: String): Value = Value.StructAt(this, name)
val Pair<String, Value>.field get() = Value.Field(first, second)

val Value.inc: Value get() = Value.Inc(this)
val Value.dec: Value get() = Value.Dec(this)

operator fun Value.plus(rhs: Value): Value = Value.Plus(this, rhs)
operator fun Value.minus(rhs: Value): Value = Value.Minus(this, rhs)
operator fun Value.times(rhs: Value): Value = Value.Times(this, rhs)

fun Type.fn(fn: Value.() -> Value): Fn = Fn(this, input.fn())
