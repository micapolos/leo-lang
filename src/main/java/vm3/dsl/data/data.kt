package vm3.dsl.data

import vm3.Data

val Boolean.bool: Data get() = Data.Bool(this)
val Int.i32: Data get() = Data.I32(this)
val Float.f32: Data get() = Data.F32(this)
fun array(vararg array: Data) = Data.Array(array.toList())
fun struct(vararg fields: Pair<String, Data>) = Data.Struct(fields.map { it.field })
val Pair<String, Data>.field get() = Data.Struct.Field(first, second)
