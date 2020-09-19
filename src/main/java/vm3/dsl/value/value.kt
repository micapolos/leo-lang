package vm3.dsl.value

import vm3.Type
import vm3.Value

fun argument(depth: Int): Value = Value.Argument(depth)
val argument = argument(0)

val Boolean.value: Value get() = Value.Bool(this)
val Int.value: Value get() = Value.I32(this)
val Float.value: Value get() = Value.F32(this)

fun array(vararg values: Value): Value = Value.Array(values.toList())
operator fun Value.get(index: Value): Value = Value.ArrayAt(this, index)

fun struct(vararg fields: Pair<String, Value>): Value = Value.Struct(fields.map { it.field })
operator fun Value.get(name: String): Value = Value.StructAt(this, name)
val Pair<String, Value>.field get() = Value.Field(first, second)

fun Value.switch(vararg functions: Value.Function): Value = Value.Switch(this, functions.toList())

val Value.inc: Value get() = Value.Inc(this)
val Value.dec: Value get() = Value.Dec(this)

operator fun Value.plus(rhs: Value): Value = Value.Plus(this, rhs)
operator fun Value.minus(rhs: Value): Value = Value.Minus(this, rhs)
operator fun Value.times(rhs: Value): Value = Value.Times(this, rhs)

fun Type.gives(result: Value) = Value.Function(this, result)
fun Value.Function.give(value: Value): Value = Value.Call(this, value)