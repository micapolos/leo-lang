package vm3.dsl.type

import vm3.type.Type

val f32 = Type.F32

fun struct(vararg fields: Pair<String, Type>): Type = Type.Struct(fields.map { it.field }.toList())
fun choice(vararg types: Type): Type = Type.Choice(types.toList())
val Pair<String, Type>.field get() = Type.Field(first, second)

operator fun Type.get(name: String): Type = (this as Type.Struct).fields.last { it.name == name }.valueType