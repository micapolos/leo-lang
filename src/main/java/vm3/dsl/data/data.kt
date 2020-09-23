package vm3.dsl.data

import vm3.data.Data

val Float.data: Data get() = Data.F32(this)
fun struct(vararg fields: Pair<String, Data>) = Data.Struct(fields.map { it.field })
val Pair<String, Data>.field get() = Data.Field(first, second)
