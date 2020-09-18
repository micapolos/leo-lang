package vm3.dsl.type

import vm3.Type

val bool = Type.Bool
val i32 = Type.I32
val f32 = Type.F32

operator fun Type.get(size: Int): Type = Type.Array(this, size)
fun struct(vararg fields: Pair<String, Type>): Type = Type.Struct(fields.map { it.field }.toList())
val Pair<String, Type>.field get() = Type.Struct.Field(first, second)

val Type.item: Type get() = (this as Type.Array).itemType
operator fun Type.get(name: String): Type = (this as Type.Struct).fields.first { it.name == name }.valueType