package vm3.dsl.data

import vm3.Data

val Boolean.data: Data get() = Data.Bool(this)
val Int.data: Data get() = Data.I32(this)
val Float.data: Data get() = Data.F32(this)
fun array(vararg array: Data) = Data.Array(array.toList())
fun struct(vararg fields: Pair<String, Data>) = Data.Struct(fields.map { it.field })
val Pair<String, Data>.field get() = Data.Field(first, second)
